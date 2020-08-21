import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);
        return props;
    }
    public static void main(String[] args) {
        StreamsConfig streamsConfig = new StreamsConfig(getProperties());
        Serde<String> stringSerde = Serdes.String();

        StreamsBuilder builder = new StreamsBuilder();
         KStream<String,String> kStream =  builder.stream("quickstart-events", Consumed.with( stringSerde, stringSerde));
//         kStream.foreach((k,v)-> System.out.println("key is = "+k.toString()+" value is ="+v.toString()));

//        KTable<Windowed<String>,Long> tweetCountTable = kStream.groupByKey().aggregate(
//                //()->0L,
//                (aggkey,value, aggregate) ->  1L ,
//                Serdes.Long()
//        );

        kStream.map((k,v)-> new KeyValue<>(k, Integer.parseInt(v) )).groupByKey(Grouped.with(Serdes.String(), Serdes.Integer())).reduce(Integer::sum).toStream().to("tweet-count",Produced.with(Serdes.String(), Serdes.Integer()));
//        builder.build();

//        kStream.to
//
//                .flatMapValues(text -> asList(text.split(" ")))
//                .map((key, word) -> new KeyValue<>(word, word))
//                .countByKey(stringSerde, "Counts")
//                .toStream()
//                .map((word, count) -> new KeyValue<>(word, word + ":" + count))
//                .to(stringSerde, stringSerde, "counts-topic");
        kStream.foreach((k,v)-> System.out.println("key is = "+k.toString()+" value is ="+v.toString()));

        KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), streamsConfig);
        kafkaStreams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}

