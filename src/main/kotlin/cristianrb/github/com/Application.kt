package cristianrb.github.com

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import cristianrb.github.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    val dataSourceConfig = createDataSourceConfig(environment.config)
    val dataSource = createDataSource(dataSourceConfig)
    flywayMigrate(dataSource, dataSourceConfig)

    configureKoin(dataSource, dataSourceConfig)
    configureSerialization()
    configureMonitoring()
    configureSecurity()
    configureRouting()
    configureStatusPages()
}

private fun createDataSourceConfig(applicationConfig: ApplicationConfig) = DataSourceConfig(
    applicationConfig.property("ktor.datasource.jdbcUrl").getString(),
    applicationConfig.property("ktor.datasource.username").getString(),
    applicationConfig.property("ktor.datasource.password").getString(),
    applicationConfig.property("ktor.datasource.schema").getString()
)

private fun createDataSource(dataSourceConfig: DataSourceConfig): HikariDataSource {
    val hikariConfig = HikariConfig()
    hikariConfig.username = dataSourceConfig.username
    hikariConfig.password = dataSourceConfig.password
    hikariConfig.jdbcUrl = "jdbc:${dataSourceConfig.jdbcUrl}"
    hikariConfig.schema = dataSourceConfig.schema
    hikariConfig.maximumPoolSize = 10

    return HikariDataSource(hikariConfig)
}

private fun flywayMigrate(dataSource: DataSource, dataSourceConfig: DataSourceConfig) {
    println("Migrating...")
    val flyway = Flyway.configure()
        .dataSource(dataSource)
        .schemas(dataSourceConfig.schema)
        .load()

    flyway.migrate()
}