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
import org.apache.beam.sdk.values.Row;
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
        p.apply("Selecting", JdbcIO.readRows()
                .withDataSourceConfiguration(JdbcIO.DataSourceConfiguration.create(
                                "org.postgresql.Driver",
                                "jdbc:postgresql://localhost:5432/main")
                        .withUsername("myuser")
                        .withPassword("mypassword"))
                        .withQuery("select first_name, last_name from person where first_name = 'john'")
                .withOutputParallelization(false)
        )
                .apply("next step", MapElements.via(new SimpleFunction<Row, UserData>() {
                    @Override
                    public UserData apply(Row input) {
                        log.info(input.getValue(1));
                        return new UserData(input.getString(1), input.getString(2));
                    }
                }));

        p.run();
    }
}
