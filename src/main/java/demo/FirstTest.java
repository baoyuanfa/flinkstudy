package demo;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class FirstTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        DataStream text = env.readTextFile ("src/main/file/input/input.txt");

        SingleOutputStreamOperator returns = text.map(new MapFunction() {
            public Tuple2<String, Integer> map(Object o) throws Exception {
                String[] s = o.toString().split(",");
                Tuple2<String, Integer> tuple2 = new Tuple2<String, Integer>();
                tuple2.f0 = s[0];
                tuple2.f1 = Integer.valueOf(s[1]);

                return tuple2;
            }
        }).returns(TypeInformation.of(new TypeHint<Tuple2<String, Integer>>() { }));

        SingleOutputStreamOperator sum = returns.keyBy(0).sum(1);

        sum.setParallelism(1).writeAsText("src/main/file/output");

        env.execute();
    }
}
