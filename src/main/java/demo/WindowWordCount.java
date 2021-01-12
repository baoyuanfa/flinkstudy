package demo;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;

public class WindowWordCount {
    private static final Logger logger = Logger.getLogger(WindowWordCount.class);

    public static void main(String[] args) throws Exception {


        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Tuple2<String, Integer>> dataStream = null;

        try {
            dataStream = env
                    .socketTextStream("hadoop101", 9999)
                    .flatMap(new Splitter())
                    .keyBy(value -> value.f0)
                    .timeWindow(Time.seconds(15))
                    .sum(1)
                    .setParallelism(5);
        }catch (Exception e) {
            logger.error("error" + e);
        }


        dataStream.print();

        logger.info("result" + dataStream);
        logger.warn("warn" + dataStream);


        env.execute("Window WordCount");
    }

    public static class Splitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String sentence, Collector<Tuple2<String, Integer>> out) throws Exception {
            // a 5
            /**
             * string -> split -> []
             * [] -> tuple2
             * _1, _2
             */

            /*for (String word: sentence.split(" ")) {
                out.collect(new Tuple2<String, Integer>(word, 1));
            }*/
            String[] s = sentence.split(" ");
            Tuple2<String, Integer> tuple2 = new Tuple2<>();
            tuple2.f0=s[0];
            tuple2.f1=Integer.valueOf(s[1]);

            out.collect(tuple2);
        }
    }

}