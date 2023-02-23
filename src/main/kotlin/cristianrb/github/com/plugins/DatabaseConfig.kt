package cristianrb.github.com.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.flywaydb.core.Flyway
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.conf.MappedSchema
import org.jooq.conf.RenderMapping
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import javax.sql.DataSource

data class DataSourceConfig(
    val username: String,
    val password: String,
    val host: String,
    val port: String,
    val database: String,
    val schema: String
)

class JooqConfiguration(dataSource: HikariDataSource, dataSourceConfig: DataSourceConfig) {

    val dslContext: DSLContext

    init {
        dslContext = createDSLContext(dataSource, dataSourceConfig)
    }

    private fun createDSLContext(dataSource: DataSource, dataSourceConfig: DataSourceConfig): DSLContext {
        val settings = Settings()
            .withRenderMapping(
                RenderMapping()
                    .withSchemata(
                        MappedSchema().withInput("public")
                            .withOutput(dataSourceConfig.schema)
                    )
            )

        return DSL.using(dataSource, SQLDialect.POSTGRES, settings)
    }
}