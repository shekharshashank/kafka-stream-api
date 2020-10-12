import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.errors.LogAndFailExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import org.json.simple.parser.JSONParser;
import serdes.AppSerdes;
import types.Retweet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author shashshe
 */
public class TwitterStreamApi {
    private static Properties getProperties() {
        Properties props = new Properties();
        props.put(StreamsConfig.CLIENT_ID_CONFIG, "kafka-stream-example");
        props.put("group.id", "test-group");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-counter");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        props.put("default.deserialization.exception.handler", LogAndFailExceptionHandler.class);
        return props;
    }

    public static void main(String[] args) {
        StreamsConfig streamsConfig = new StreamsConfig(getProperties());
        Properties props = getProperties();

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> kStream0 = builder.stream("quickstart-events", Consumed.with(Serdes.String(), AppSerdes.String()));



         kStream0.mapValues(retweetString -> getRetweet(retweetString)).to("retweet-1-topic",Produced.with(AppSerdes.String(),AppSerdes.Retweet()));
//
          KStream<String, String> kStream1 = builder.stream("retweet-1-topic", Consumed.with(AppSerdes.String(), AppSerdes.String()));

        kStream1.foreach((k, v) -> {

            System.out.println(v);

        });
//
//          KTable<String,Retweet> ktable1 = kStream1.toTable();
//        System.out.println("ktable1=========>"+ktable1);




        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, props);

        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            streams.close();
        }));
    }

    public static Retweet getRetweet(String strRetweet) {
        JSONParser parser = new JSONParser();
        final ObjectReader reader = new ObjectMapper()
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
                .reader();
        final JsonNode node;
        try {
            node = reader.readTree(strRetweet);
        } catch (IOException e) {
            return new Retweet();
        }
        Retweet retweet = new Retweet();
        return retweet.withScreenName(node.get("screen_name").asText()).withText(node.get("text").asText()).withFollowersCount(node.get("followers_count").asInt()).withUserId(node.get("user_id").asText());

    }
}

