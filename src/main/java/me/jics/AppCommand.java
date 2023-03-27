package me.jics;

import io.micronaut.configuration.picocli.PicocliRunner;

import lombok.extern.slf4j.Slf4j;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import picocli.CommandLine.Command;


@Slf4j
@Command(name = "app", description = "...",
        mixinStandardHelpOptions = true)
public class AppCommand implements Runnable {

    public static void main(String[] args) throws Exception {
        int code = PicocliRunner.execute(AppCommand.class, args);
        System.exit(code);
    }

    public void run() {
        AppOptions options = PipelineOptionsFactory
                .fromArgs("--runner=DirectRunner")
                .as(AppOptions.class);
        Pipeline p = Pipeline.create(options);
        p.apply("Selecting", JdbcIO.<UserData>read()
                .withDataSourceConfiguration(JdbcIO.DataSourceConfiguration.create(
                                "org.postgresql.Driver",
                                "jdbc:postgres://localhost:5432/mydb")
                        .withUsername("myuser")
                        .withPassword("mypassword"))
                        .withQuery("select name, lastname from user where name = 'john'")
                .withRowMapper(resultSet -> UserData.builder()
                        .name(resultSet.getString(1))
                        .lastname(resultSet.getString(2))
                        .build())
                .withOutputParallelization(false)
        )
                .apply("nex step", MapElements.via(new SimpleFunction<UserData, Integer>() {
                    @Override
                    public Integer apply(UserData input) {
                        log.info(input.toString());
                        return 1;
                    }
                }));

        p.run();
    }
}
