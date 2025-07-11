package software.amazon.app.platform.template

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import software.amazon.app.platform.template.templates.AppTemplate
import kotlinx.coroutines.flow.StateFlow
import software.amazon.app.platform.scope.RootScopeProvider
import software.amazon.app.platform.scope.di.diComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

/**
 * `ViewModel` that hosts the stream of templates and survives configuration changes. Note that we
 * use [application] to get access to the root scope.
 */
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val component = (application as RootScopeProvider).rootScope.diComponent<Component>()
    private val templateProvider = component.templateProviderFactory.createTemplateProvider()

    /** The stream of templates that are rendered by [MainActivity]. */
    val templates: StateFlow<AppTemplate> = templateProvider.templates

    override fun onCleared() {
        templateProvider.cancel()
    }

    /** Component interface to give us access to objects from the app component. */
    @ContributesTo(AppScope::class)
    interface Component {
        /** Gives access to the [TemplateProvider.Factory] from the object graph. */
        val templateProviderFactory: TemplateProvider.Factory
    }
}
