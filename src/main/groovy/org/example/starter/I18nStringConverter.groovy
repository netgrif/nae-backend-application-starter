package org.example.starter

import com.netgrif.application.engine.petrinet.domain.I18nString
import org.codehaus.groovy.runtime.DefaultGroovyMethods
import org.codehaus.groovy.runtime.StringGroovyMethods


class I18nStringConverter {

    static Object asType(String self, Class target) {
        if (target == I18nString) {
            return new I18nString(self)
        }
        return StringGroovyMethods.asType(self, target)
    }

    static Object asType(GString self, Class target) {
        if (target == I18nString) {
            return new I18nString(self.toString())
        }
        return DefaultGroovyMethods.asType(self, target)
    }
}
