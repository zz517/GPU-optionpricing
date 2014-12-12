
package class6;
import com.nativelibs4java.opencl.*;
import org.bridj.Pointer;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.bridj.Pointer.allocateFloats;

/**
 * Created with IntelliJ IDEA.
 * User: eran
 * Date: 8/24/13
 * Time: 2:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Example6 {

    public static void main(String[] args){

        // Creating the platform which is out computer.
        CLPlatform clPlatform = JavaCL.listPlatforms()[0];
        // Getting the GPU device
        CLDevice device = clPlatform.getBestDevice();
        //CLDevice device = clPlatform.listGPUDevices(true)[0];
        // Verifing that we have the GPU device
        System.out.println("*** New device *** ");
        System.out.println("Vendor: " + device.getVendor());
        System.out.println("Name: " + device.getName() );
        System.out.println("Type: " + device.getType() );
        // Let's make a context
        CLContext context = JavaCL.createContext(null, device);
        // Lets make a default FIFO queue.
        CLQueue queue = context.createDefaultQueue();

        // Read the program sources and compile them :
        String src = "__kernel void add_floats(__global const float* a, __global const float* b, __global float* out, int n) \n" +
                "{\n" +
                "    int i = get_global_id(0);\n" +
                "    if (i >= n/2)\n" +
                "        return;\n" +
                "\n" +
                "    out[i] = a[i] + b[i];\n" +
                "}\n" +
                "\n" +
                "__kernel void fill_in_values(__global float* a, __global float* b, int n) \n" +
                "{\n" +
                "    int i = get_global_id(0);\n" +
                "    if (i >= n)\n" +
                "        return;\n" +
                "\n" +
                "    a[i] = cos((float)i);\n" +
                "    b[i] = sin((float)i);\n" +
                "}";
        CLProgram program = context.createProgram(src);
        program.build();

        CLKernel kernel = program.createKernel("add_floats");

        final long tmp = System.currentTimeMillis();
        final int n = 1024*256*16;
        final Pointer<Float>
                aPtr = allocateFloats(n),
                bPtr = allocateFloats(n);

    //    System.out.println((System.currentTimeMillis() - tmp));

        for (int i = 0; i < n; i++) {
            aPtr.set(i, (float) i);
            bPtr.set(i, (float) 2*i);
        }

    //    System.out.println((System.currentTimeMillis() - tmp));

        // Create OpenCL input buffers (using the native memory pointers aPtr and bPtr) :
        CLBuffer<Float>
                a = context.createFloatBuffer(CLMem.Usage.Input, aPtr),
                b = context.createFloatBuffer(CLMem.Usage.Input, bPtr);

        // Create an OpenCL output buffer :
        CLBuffer<Float> out = context.createFloatBuffer(CLMem.Usage.Output, n);
        kernel.setArgs(a, b, out, n);
   //     System.out.println((System.currentTimeMillis() - tmp));
        CLEvent event = kernel.enqueueNDRange(queue, new int[]{n}, new int[]{128});
        event.invokeUponCompletion(new Runnable() {
            @Override
            public void run() {
               // System.out.println((System.currentTimeMillis() - tmp));
            }
        });
        final Pointer<Float> cPtr = out.read(queue,event);
    //    System.out.println((System.currentTimeMillis() - tmp));
                for ( int i = 0; i < 12; ++i){
                   // System.out.println( aPtr.get(i) + " " + cPtr.get(i) );
                }
        System.exit(0);
        float[] aa = new float[n];
        float[] bb = new float[n];
        float[] cc = new float[n];

        for ( int i = 0; i < n; ++i){
            aa[i] = (float) i;
            bb[i] = (float) i;
        }
      //  System.out.println((System.currentTimeMillis() - tmp));
        for ( int i = 0; i < n; ++i){
            cc[i] = aa[i] + bb[i];
        }

    //    System.out.println((System.currentTimeMillis() - tmp));











    }

}
