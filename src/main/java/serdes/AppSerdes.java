package serdes;


import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import types.Retweet;

/**
 * @author shashshe
 */
public class AppSerdes extends Serdes {

    public static final class RetweetSerde extends WrapperSerde<Retweet> {
        public RetweetSerde() {
            super(new JsonSerializer(), new JsonDeserializer());
        }
    }

    static public Serde<Retweet> Retweet(){
        return new RetweetSerde();
    }
}
