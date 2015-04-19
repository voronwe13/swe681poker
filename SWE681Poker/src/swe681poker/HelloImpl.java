package swe681poker;

import java.io.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class HelloImpl implements Hello {
  
    public HelloImpl() {}

    public String sayHello() {
        return  "Hello World!";
    }
  
    public static void main(String args[]) {

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        byte pattern = (byte) 0xAC;
        try {
            /*
             * Create remote object and export it to use
             * custom socket factories.
             */
            HelloImpl obj = new HelloImpl();
            RMIClientSocketFactory csf = new XorClientSocketFactory(pattern);
            RMIServerSocketFactory ssf = new XorServerSocketFactory(pattern);
            Hello stub =
                (Hello) UnicastRemoteObject.exportObject(obj, 0, csf, ssf);
            
            /*
             * Create a registry and bind stub in registry.
             */
            LocateRegistry.createRegistry(2002);
            Registry registry = LocateRegistry.getRegistry(2002);       
            registry.rebind("Hello", stub);
            System.out.println("HelloImpl bound in registry");
            
        } catch (Exception e) {
            System.out.println("HelloImpl exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
