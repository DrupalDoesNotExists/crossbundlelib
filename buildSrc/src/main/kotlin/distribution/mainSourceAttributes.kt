@file:Suppress("UnstableApiUsage")

package distribution

import org.gradle.api.artifacts.Configuration
import org.gradle.api.attributes.Bundling.BUNDLING_ATTRIBUTE
import org.gradle.api.attributes.Bundling.EXTERNAL
import org.gradle.api.attributes.Category.CATEGORY_ATTRIBUTE
import org.gradle.api.attributes.Category.VERIFICATION
import org.gradle.api.attributes.VerificationType.MAIN_SOURCES
import org.gradle.api.attributes.VerificationType.VERIFICATION_TYPE_ATTRIBUTE
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.named

fun Configuration.mainSourcesAttributes(objects: ObjectFactory): Configuration {
    return attributes {
        attribute(CATEGORY_ATTRIBUTE, objects.named(VERIFICATION))
        attribute(BUNDLING_ATTRIBUTE, objects.named(EXTERNAL))
        attribute(VERIFICATION_TYPE_ATTRIBUTE, objects.named(MAIN_SOURCES))
    }
}
