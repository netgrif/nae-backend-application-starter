package org.example.starter.startup

import com.netgrif.application.engine.menu.domain.MenuItemBody
import com.netgrif.application.engine.menu.domain.configurations.CaseViewBody
import com.netgrif.application.engine.menu.domain.dashboard.DashboardItemBody
import com.netgrif.application.engine.menu.domain.dashboard.DashboardManagementBody
import com.netgrif.application.engine.menu.domain.templates.CustomViewTemplate
import com.netgrif.application.engine.menu.domain.templates.FolderTemplate
import com.netgrif.application.engine.menu.domain.templates.SimpleTaskViewTemplate
import com.netgrif.application.engine.menu.domain.templates.TabbedCaseViewTemplate
import com.netgrif.application.engine.menu.service.MenuItemTemplateHolder
import com.netgrif.application.engine.menu.service.interfaces.DashboardItemService
import com.netgrif.application.engine.menu.service.interfaces.DashboardManagementService
import com.netgrif.application.engine.menu.service.interfaces.IMenuItemService
import com.netgrif.application.engine.petrinet.domain.I18nString
import com.netgrif.application.engine.petrinet.service.interfaces.IPetriNetService
import com.netgrif.application.engine.startup.AbstractOrderedCommandLineRunner
import com.netgrif.application.engine.workflow.domain.Case
import groovy.util.logging.Slf4j
import org.example.starter.CustomActionDelegate
import org.springframework.stereotype.Component

@Slf4j
@Component
class ViewConfigurationRunner extends AbstractOrderedCommandLineRunner {

    protected final IPetriNetService petriNetService
    protected final CustomActionDelegate actionDelegate
    protected final IMenuItemService menuItemService
    protected final DashboardManagementService dashboardManagementService
    protected final DashboardItemService dashboardItemService

    ViewConfigurationRunner(
            IPetriNetService petriNetService,
            CustomActionDelegate actionDelegate,
            DashboardManagementService dashboardManagementService,
            DashboardItemService dashboardItemService,
            IMenuItemService menuItemService
    ) {
        this.petriNetService = petriNetService
        this.actionDelegate = actionDelegate
        this.dashboardManagementService = dashboardManagementService
        this.dashboardItemService = dashboardItemService
        this.menuItemService = menuItemService
        this.allIdentifiers = petriNetService.getAll().collect { it.identifier }
        log.info("All nets: ${allIdentifiers}")
    }
    private List<String> allIdentifiers = []
    private Case tutorialFolder, settingsFolder, serviceDeskFolder

    @Override
    void run(String... args) throws Exception {
        createFolders()
        createDefaultViews(tutorialFolder.dataSet["nodePath"].value as String)
        createServiceDeskViews(serviceDeskFolder.dataSet["nodePath"].value as String)
        createSettingsViews(settingsFolder.dataSet["nodePath"].value as String)
        configureDashboard()
    }

    void createFolders() {
        def defaultFolder = MenuItemTemplateHolder.get(FolderTemplate.IDENTIFIER, "/", new I18nString("Default Menu Item")).get()
        defaultFolder.menuIcon = "device_hub"
        defaultFolder.autoSelect = true
        this.tutorialFolder = menuItemService.createOrIgnoreMenuItem(defaultFolder)

        def sdFolder = MenuItemTemplateHolder.get(FolderTemplate.IDENTIFIER, "/", new I18nString("Service Desk")).get()
        sdFolder.menuIcon = "support_agent"
        this.serviceDeskFolder = menuItemService.createOrIgnoreMenuItem(sdFolder)

        def settingsFolder = MenuItemTemplateHolder.get(FolderTemplate.IDENTIFIER, "/", new I18nString("Settings")).get()
        settingsFolder.menuIcon = "settings"
        this.settingsFolder = menuItemService.createOrIgnoreMenuItem(settingsFolder)
    }

    void createDefaultViews(String folderUri) {
        MenuItemBody allCasesMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("All cases", [
                        "sk": "Všetky prípady",
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        allCasesMenuItem.menuIcon = "assignment"
        allCasesMenuItem.autoSelect = true
        menuItemService.createOrIgnoreMenuItem(allCasesMenuItem)

        MenuItemBody myCasesMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("My cases", [
                        "sk": "Moje prípady",
                        "de": "Meine Fälle",
                        "cz": "Moje případy",
                ])
        ).get()
        myCasesMenuItem.menuIcon = "assignment_ind"
        myCasesMenuItem.view.filterBody.query = "cases: author == '<<me>>'"
        menuItemService.createOrIgnoreMenuItem(myCasesMenuItem)

        MenuItemBody allTasksMenuItem = MenuItemTemplateHolder.get(
                SimpleTaskViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("All tasks", [
                        "sk": "Všetky úlohy",
                        "de": "Alle Aufgaben",
                        "cz": "Všechny úkoly",
                ])
        ).get()
        allTasksMenuItem.menuIcon = "library_add_check"
        menuItemService.createOrIgnoreMenuItem(allTasksMenuItem)

        MenuItemBody myTasksMenuItem = MenuItemTemplateHolder.get(
                SimpleTaskViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("My tasks", [
                        "sk": "Moje úlohy",
                        "de": "Meine Aufgaben",
                        "cz": "Moje úkoly",
                ])
        ).get()
        myTasksMenuItem.menuIcon = "assignment"
        myTasksMenuItem.view.filterBody.query = "tasks: userId == '<<me>>'"
        menuItemService.createOrIgnoreMenuItem(myTasksMenuItem)
    }

    void createSettingsViews(String folderUri) {
        MenuItemBody processesMenuItem = MenuItemTemplateHolder.get(
                CustomViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Processes", [
                        "sk": "Procesy",
                        "de": "Prozesse",
                        "cz": "Procesy",
                ])
        ).get()
        processesMenuItem.menuIcon = "device_hub"
        processesMenuItem.autoSelect = true
        processesMenuItem.customViewSelector = "builder"
        menuItemService.createOrIgnoreMenuItem(processesMenuItem)

        MenuItemBody roleManagementMenuItem = MenuItemTemplateHolder.get(
                CustomViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Role management", [
                        "sk": "Správa rolí",
                        "de": "Rollenmanagement",
                        "cz": "Správa rolí",
                ])
        ).get()
        roleManagementMenuItem.menuIcon = "psychology"
        roleManagementMenuItem.customViewSelector = "console"
        menuItemService.createOrIgnoreMenuItem(roleManagementMenuItem)

        MenuItemBody menuItemsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Menu items", [
                        "sk": "Položky menu",
                        "de": "Menüpunkte",
                        "cz": "Položky menu",
                ])
        ).get()
        menuItemsMenuItem.menuIcon = "menu_open"
        CaseViewBody menuItemsView = menuItemsMenuItem.view as CaseViewBody
        menuItemsView.filterBody.query = "cases: processIdentifier == 'menu_item'"
        menuItemsView.createCaseButtonIcon = "playlist_add"
        menuItemsView.createCaseButtonTitle = new I18nString("Create Menu Item", ["sk": "Vytvor položku menu", "de": "Menüpunkt erstellen"])
        menuItemsView.showMoreMenu = true
        menuItemsView.allAllowedNets = false
        menuItemsView.allowedNets = ["menu_item"]
        menuItemsView.headersSortModeActive = "menu_item-nodePath"
        menuItemsView.headersSortModeDirection = "asc"
        menuItemsView.defaultHeaders = ["meta-title", "menu_item-nodePath", "menu_item-menu_item_identifier", "menu_item-view_configuration_type"]
        menuItemsView.requireTitleInCreation = false
        menuItemService.createOrIgnoreMenuItem(menuItemsMenuItem)

        MenuItemBody dashboardMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Dashboard", [
                        "sk": "Dashboard",
                        "de": "Dashboard",
                        "cz": "Dashboard",
                ])
        ).get()
        dashboardMenuItem.menuIcon = "dashboard"
        CaseViewBody dashboardView = dashboardMenuItem.view as CaseViewBody
        dashboardView.filterBody.query = "cases: processIdentifier in ('dashboard_item', 'dashboard_management')"
        dashboardView.createCaseButtonIcon = "dashboard_customize"
        dashboardView.createCaseButtonTitle = new I18nString("Create Dashboard Item", ["sk": "Vytvor položku dashboardu", "de": "Dashboard-Element erstellen"])
        dashboardView.showMoreMenu = true
        menuItemService.createOrIgnoreMenuItem(dashboardMenuItem)
    }

    void createServiceDeskViews(String folderUri) {
        MenuItemBody allTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("All tickets", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        allTicketsMenuItem.menuIcon = "local_activity"
        allTicketsMenuItem.autoSelect = true
        CaseViewBody ticketViewBody =  allTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system'"
        ticketViewBody.requireTitleInCreation = false
        ticketViewBody.allAllowedNets = false
        ticketViewBody.allowedNets = ["sd_system"]
        ticketViewBody.createCaseButtonIcon = "add"
        ticketViewBody.createCaseButtonTitle = new I18nString("Create New Ticket")
        ticketViewBody.defaultHeaders = [
                "meta-author",
                "meta-creationDate",
                "sd_system-category",
                "sd_system-phase_txt",
                "sd_system-assigned_user"
        ]
        menuItemService.createOrIgnoreMenuItem(allTicketsMenuItem)

        MenuItemBody newTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("New tickets", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        newTicketsMenuItem.menuIcon = "add_2"
        ticketViewBody =  newTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system' and data.phase_txt.value == 'New'"
        ticketViewBody.showCreateCaseButton = false
        menuItemService.createOrIgnoreMenuItem(newTicketsMenuItem)

        MenuItemBody rejectedTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Rejected", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        rejectedTicketsMenuItem.menuIcon = "block"
        ticketViewBody =  rejectedTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system' and data.phase_txt.value == 'Rejected'"
        ticketViewBody.showCreateCaseButton = false
        menuItemService.createOrIgnoreMenuItem(rejectedTicketsMenuItem)

        MenuItemBody resolvedTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Resolved", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        resolvedTicketsMenuItem.menuIcon = "check_circle"
        ticketViewBody =  resolvedTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system' and data.phase_txt.value == 'Resolved'"
        ticketViewBody.showCreateCaseButton = false
        menuItemService.createOrIgnoreMenuItem(resolvedTicketsMenuItem)

        MenuItemBody closedTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Closed", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        closedTicketsMenuItem.menuIcon = "stop_circle"
        ticketViewBody =  closedTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system' and data.phase_txt.value == 'Closed'"
        ticketViewBody.showCreateCaseButton = false
        menuItemService.createOrIgnoreMenuItem(closedTicketsMenuItem)

        MenuItemBody inProgressTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("In Progress", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        inProgressTicketsMenuItem.menuIcon = "timelapse"
        ticketViewBody =  inProgressTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system' and data.phase_txt.value == 'In Progress'"
        ticketViewBody.showCreateCaseButton = false
        menuItemService.createOrIgnoreMenuItem(inProgressTicketsMenuItem)

        MenuItemBody bugsTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Bugs", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        bugsTicketsMenuItem.menuIcon = "bug_report"
        ticketViewBody =  bugsTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system' and data.category.value == 'Incident Bug'"
        ticketViewBody.showCreateCaseButton = false
        menuItemService.createOrIgnoreMenuItem(bugsTicketsMenuItem)

        MenuItemBody changeRequestsTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Change Requests", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        changeRequestsTicketsMenuItem.menuIcon = "alt_route"
        ticketViewBody =  changeRequestsTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system' and data.category.value == 'Change Request'"
        ticketViewBody.showCreateCaseButton = false
        menuItemService.createOrIgnoreMenuItem(changeRequestsTicketsMenuItem)

        MenuItemBody serviceRequestsTicketsMenuItem = MenuItemTemplateHolder.get(
                TabbedCaseViewTemplate.IDENTIFIER,
                folderUri,
                new I18nString("Service Requests", [
                        "sk": "Všetky prípady",// TODO
                        "de": "Alle Fälle",
                        "cz": "Všechny případy",
                ])
        ).get()
        serviceRequestsTicketsMenuItem.menuIcon = "build"
        ticketViewBody =  serviceRequestsTicketsMenuItem.view as CaseViewBody
        ticketViewBody.filterBody.query = "cases: processIdentifier == 'sd_system' and data.category.value == '️Service Request'"
        ticketViewBody.showCreateCaseButton = false
        menuItemService.createOrIgnoreMenuItem(serviceRequestsTicketsMenuItem)
    }

    void configureDashboard() {
        Case dashboard = dashboardManagementService.findDashboardManagement("main_dashboard")
        def dashboardConfig = new DashboardManagementBody("main_dashboard", new I18nString("Main Dashboard", Map.of("sk", "Hlavný Dashboard", "de", "Haupt-Dashboard", "cz", "Hlavní Dashboard")))
        Case tutorialDashboardItem = dashboardItemService.getOrCreate(toDashboardItem(tutorialFolder))
        Case settingsDashboardItem = dashboardItemService.getOrCreate(toDashboardItem(settingsFolder))
        Case serviceDeskDashboardItem = dashboardItemService.getOrCreate(toDashboardItem(serviceDeskFolder))
        dashboardConfig.dashboardItems = [
                (tutorialDashboardItem.stringId): tutorialDashboardItem.getFieldValue("item_name"),
                (settingsDashboardItem.stringId): settingsDashboardItem.getFieldValue("item_name"),
                (serviceDeskDashboardItem.stringId): serviceDeskDashboardItem.getFieldValue("item_name")
        ]
        dashboardConfig.logo = "assets/netgrif_logo.svg"
        dashboardConfig.simpleDashboard = true
        Thread.sleep(1000)
        dashboardManagementService.updateDashboardManagement(dashboard, dashboardConfig)
    }

    DashboardItemBody toDashboardItem(Case folder) {
        def item = new DashboardItemBody(
                folder.getFieldValue("menu_item_identifier") as String,
                folder.getStringId(),
                folder.getFieldValue("menu_icon") as String,
                folder.getFieldValue("menu_name") as I18nString,
                true
        )
        item.fontColor = "#000000b3"
        item.iconColor = "#0f4c81"
        return item
    }
}
