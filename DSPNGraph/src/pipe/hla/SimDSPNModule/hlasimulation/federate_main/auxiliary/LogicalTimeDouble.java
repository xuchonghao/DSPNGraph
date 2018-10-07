package pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary;


import hla.rti1516.IllegalTimeArithmetic;
import hla.rti1516.LogicalTime;
import hla.rti1516.LogicalTimeInterval;
import se.pitch.prti1516.LogicalTimeIntervalDouble;

public class LogicalTimeDouble extends se.pitch.prti1516.LogicalTimeDouble{

    double value;
    public LogicalTimeDouble(double v) {
        super(v);
        value = v;
    }


    public double getDoubleTime() {
        return value;
    }

    @Override
    public LogicalTime add(LogicalTimeInterval logicalTimeInterval) throws IllegalTimeArithmetic {
        return new LogicalTimeDouble(value +((LogicalTimeIntervalDouble)logicalTimeInterval).getValue());
    }

    @Override
    public boolean isFinal() {
        return value == Constant.MAX_TIME;
    }
    @Override
    public int compareTo(Object var1) {
        long var2 = 0L;
        if(var1 instanceof se.pitch.prti1516.LogicalTimeDouble){
            var2 = (long) ((se.pitch.prti1516.LogicalTimeDouble)var1).getValue();
        }else if(var1 instanceof LogicalTimeDouble){
            var2 = (long) ((LogicalTimeDouble)var1).value;
        }else {
            try {
                throw new Exception("出错了，不可能有别的LogicalTimeDouble类型");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (this.value == var2) {
            return 0;
        } else if(value < var2)
            return -1;
        else
            return  1;
    }
}
