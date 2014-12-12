package homework3;

import com.nativelibs4java.opencl.*;
import org.bridj.Pointer;
import java.util.Random;
import static org.bridj.Pointer.allocateFloats;

/**
 * Created by Zhongrun on 12/12/2014.
 */
public class GPURandomNumberGenerator implements RandomVectorGenerator{

    // N normals for path
    private int N;
    //number of normals we need to generate at 1 time
    private int numberOfBatches;
    //generated vectors from GPU
    private double[] normalVector;
    // count how many normals we already get from the vector
    private int count;

    public GPURandomNumberGenerator(int N, int numberOfBatches){
        this.N=N;
        this.numberOfBatches=numberOfBatches;
        getNormalVector();
        count=0;
    }

    @Override
    public double[] getVector(){
        double[] vector = new double[N];

        for(int i = 0; i < N; i++){
            // if the idx reaches the generated amount of normal random number
            // we regenerate a batch of random number again
            if(count == numberOfBatches - 1) {
                getNormalVector();
            }
            // we assign the generated normal to the vector needed for the simulation
            vector[i] = normalVector[count];
            count++;
        }
        return vector;
    }
    public void getNormalVector(){
        count = 0 ;

        // Creating the platform which is out computer.
        CLPlatform clPlatform = JavaCL.listPlatforms()[0];
        // Getting the GPU device
        CLDevice device = clPlatform.getBestDevice();
        // Make a context
        CLContext context = JavaCL.createContext(null, device);
        // Lets make a default FIFO queue.
        CLQueue queue = context.createDefaultQueue();


        /**
         * I tried three different methods. First two works fine.
         * I don't know why last one can not work.
         *
         */
        // Read the program sources and compile them :
        String src = "__kernel void convert_to_gaussian(__global const float* a, __global const float* b, __global float* outA, __global float* outB, float pi, int n) \n" +
                "{\n" +
                "    int i = get_global_id(0);\n" +
                "    if (i >= n)\n" +
                "        return;\n" +
                "\n" +
                "    outA[i] = sqrt(-2*log(a[i]))*cos(2*pi*b[i]);\n" +
              //  "    outA[2*i+1] = sqrt(-2*log(a[i]))*cos(2*pi*b[i]);\n" +
                "    outB[i] = sqrt(-2*log(a[i]))*sin(2*pi*b[i]);\n"+
                "}\n"+
                "__kernel void convert_to_gaussian1(__global float* a, __global float* out, float pi,int n) \n" +
                "{\n" +
                "    int i = get_global_id(0);\n" +
                "    if (i >= n / 2)\n" +
                "        return;\n" +
                "\n" +
                "    out[2*i] = sqrt(-2*log(a[2*i]))*cos(2*pi*a[2*i+1]);\n" +
                "    out[2*i+1] = sqrt(-2*log(a[2*i]))*sin(2*pi*a[2*i+1]);\n" +
                "}"+
                "\n" +
                 "__kernel void convert_to_gaussian2(__global float* a, __global float* out, float pi,int n) \n" +
                "{\n" +
                "    int i = get_global_id(0);\n" +
                "    if (i >= n )\n" +
                "        return;\n" +
                "\n" +
                "    out[i] = sqrt(-2*log(a[i]))*cos(2*pi*a[i+1]);\n" +
                "    out[i+1] = sqrt(-2*log(a[i]))*sin(2*pi*a[i+1]);\n" +
                "    i=i+2;\n"+
                "}";

        CLProgram program = context.createProgram(src);

        program.build();

        CLKernel kernel = program.createKernel("convert_to_gaussian2");

        final int n = numberOfBatches;
        final Pointer<Float>
                aPtr = allocateFloats(n);
        //        bPtr = allocateFloats(n);

        // create new uniform Vectors
        double[] uniformVectors = (new UniformRandomNumberGenerator(n)).getVector();
        // pass to A pointer
        for (int i = 0; i < n; i++) {
            aPtr.set(i, (float) uniformVectors[i]);
        //    bPtr.set(i, (float) uniformVectors[2*i+1]);
        //    System.out.println(uniformVectors[i]);
          //  System.out.println(uniformVectors[2*i+1]);
        }

        //create buffer for input
        CLBuffer<Float>
                a = context.createFloatBuffer(CLMem.Usage.Input, aPtr);
        //        b = context.createFloatBuffer(CLMem.Usage.Input, bPtr);
        //create buffer for output
        CLBuffer<Float>
                outA = context.createFloatBuffer(CLMem.Usage.Output, n);
        //        outB = context.createFloatBuffer(CLMem.Usage.Output, n);

        //run the arguments
    //    kernel.setArgs(a,b, outA, outB,(float) Math.PI, n);
        kernel.setArgs(a, outA,(float) Math.PI, n);

        CLEvent event = kernel.enqueueNDRange(queue, new int[]{n}, new int[]{128});


        final Pointer<Float> outAPtr = outA.read(queue,event);
    //    final Pointer<Float> outBPtr = outB.read(queue,event);
        normalVector = new double[n];

          for(int i=0;i< n ;i++) {
              normalVector[i] = (double) outAPtr.get(i);
    //          normalVector[2*i] = (double) outAPtr.get(i);
    //          normalVector[2*i+1] = (double) outBPtr.get(i);
          }
      //  for(int i=0;i< n ;i++) {
      //     System.out.println( normalVector[i]);
      //  }

        // release the space
        a.release();
     //   b.release();
        aPtr.release();
     //   bPtr.release();
        outAPtr.release();
    //    outBPtr.release();

    }
}
