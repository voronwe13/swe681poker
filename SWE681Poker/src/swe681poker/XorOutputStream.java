package swe681poker;

import java.io.*;

class XorOutputStream extends FilterOutputStream {
  
    /*
     * The byte used to "encrypt" each byte of data.
     * Default is 10101010. 
     */
    private byte pattern = (byte)(170 & 0xFF);
  
    /* 
     * Constructor for class XorOutputStream
     */
    public XorOutputStream(OutputStream out, byte pattern) {
        super(out);
        if (pattern != 0)
	    this.pattern = pattern;
    }
  
    /*
     * XOR's the byte being written with the pattern
     * and writes the result.  
     */
    public void write(int b) throws IOException {
       out.write(b ^ pattern);
       out.flush();
    }
  
    /* 
     * Writes len bytes 
     */
    public void write(byte b[], int off, int len) throws IOException {
        for (int i = 0; i < len; i++)
            write(b[off + i]);
    }
}
