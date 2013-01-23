/*******************************************************************************
 * Copyright (C) 2012 BJ Peter DeLaCruz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.bpd.rmi.client;

import java.math.BigDecimal;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.bpd.rmi.api.Compute;
import com.bpd.rmi.api.Pi;

/**
 * Computes PI to the specified number of digits. The actual computation is done on the server.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ComputePi {

  /**
   * Sends a task to compute PI to the specified number of digits to the server and displays the
   * results.
   * 
   * @param args The name of the remote host and the number of decimal places to use in the
   * calculation.
   */
  public static void main(String args[]) {
    if (args.length != 2) {
      String msg = "Need two arguments:\n";
      msg += "  1. Name of remote host\n";
      msg += "  2. Number of decimal places to use in calculation";
      System.err.println(msg);
      System.exit(1);
    }
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      String name = "Compute";
      Registry registry = LocateRegistry.getRegistry(args[0], 0);
      Compute comp = (Compute) registry.lookup(name);
      Pi task = new Pi(Integer.parseInt(args[1]));
      BigDecimal pi = comp.executeTask(task);
      System.out.println(pi);
    }
    catch (Exception e) {
      System.err.println("ComputePi exception:");
      e.printStackTrace();
    }
  }

}
