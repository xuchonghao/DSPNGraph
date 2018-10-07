package pipe.hla.SimDSPNModule.hlasimulation;

import hla.rti1516.RTIexception;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.MainLoopFed;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class SenonderyThread implements Runnable {
    OLGraph subGraph;
    String subNetName;
    CyclicBarrier barrier;
    AtomicInteger atomicInteger;
    int len;
    public SenonderyThread(OLGraph subGraph, String subNetName,CyclicBarrier barrier, AtomicInteger atomicInteger,int len){
        this.subGraph = subGraph;
        this.subNetName = subNetName;
        this.barrier = barrier;
        this.atomicInteger = atomicInteger;
        this.len = len;
    }
    @Override
    public void run() {
        try {
            barrier.await();
            long start = System.currentTimeMillis();
            new MainLoopFed(subGraph,subNetName,atomicInteger,len).run();
            long end = System.currentTimeMillis();
            long value = end - start;
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~去掉sleep时间的运行时间运行时间是："+(value-4000));
        } catch (RTIexception rtIexception) {
            rtIexception.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
