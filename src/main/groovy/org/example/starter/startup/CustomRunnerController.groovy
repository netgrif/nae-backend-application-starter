package org.example.starter.startup

import com.netgrif.application.engine.startup.*

class CustomRunnerController extends RunnerController {

    private List order = [
            ElasticsearchRunner,
            MongoDbRunner,
            StorageRunner,
            RuleEngineRunner,
            DefaultRoleRunner,
            AnonymousRoleRunner,
            AuthorityRunner,
            SystemUserRunner,
            UriRunner,
            FunctionsCacheRunner,
            ProcessRunner,
            MenuRunner,
            GroupRunner,
            ImpersonationRunner,
            DashboardRunner,
            DashboardManagementRunner,
            SuperCreator,
            FlushSessionsRunner,
            MailRunner,
            PostalCodeImporter,
            DefaultDashboardRunner,
            // CUSTOM IMPORT RUNNERS
            CustomRunner,
            // END OF CUSTOM IMPORT RUNNERS
            DemoRunner,
            QuartzSchedulerRunner,
            PdfRunner,
            // ADDITIONAL CUSTOM RUNNERS
            ViewConfigurationRunner,
            TestUserRunner,
            // END OF ADDITIONAL CUSTOM RUNNERS
            FinisherRunnerSuperCreator,
            FinisherRunner,
    ]

    @Override
    protected List getOrderList() {
        return order
    }
}
