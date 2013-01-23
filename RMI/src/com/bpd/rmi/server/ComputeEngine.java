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
package com.bpd.rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import com.bpd.rmi.api.Compute;
import com.bpd.rmi.api.Task;

/**
 * A compute engine for executing tasks from a client.
 * 
 * @author BJ Peter DeLaCruz
 */
public class ComputeEngine implements Compute {

  /** {@inheritDoc} */
  @Override
  public <T> T executeTask(Task<T> task) throws RemoteException {
    if (task == null) {
      throw new IllegalArgumentException("task is null");
    }
    return task.execute();
  }

  /**
   * Sets up the ComputeEngine.
   * 
   * @param args None.
   */
  public static void main(String[] args) {
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }
    try {
      String name = "Compute";
      Compute engine = new ComputeEngine();
      Compute stub = (Compute) UnicastRemoteObject.exportObject(engine, 0);
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind(name, stub);
      System.out.println("ComputeEngine bound.");
    }
    catch (Exception e) {
      System.err.println("ComputeEngine exception: ");
      e.printStackTrace();
    }
  }

}
