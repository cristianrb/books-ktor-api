package cristianrb.github.com.plugins

import cristianrb.github.com.modules.BooksController
import cristianrb.github.com.modules.BooksControllerImpl
import cristianrb.github.com.repository.BooksRepository
import cristianrb.github.com.repository.BooksRepositoryImpl
import cristianrb.github.com.service.BooksService
import cristianrb.github.com.service.BooksServiceImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(
            module {
                single<BooksController> { BooksControllerImpl() }
                single<BooksService> { BooksServiceImpl() }
                single<BooksRepository> { BooksRepositoryImpl() }
                single { JooqConfiguration(environment.config) }
            }
        )
    }
}