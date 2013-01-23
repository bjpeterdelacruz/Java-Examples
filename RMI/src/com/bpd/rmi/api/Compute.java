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
package com.bpd.rmi.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The interface for the compute engine.
 * 
 * @author BJ Peter DeLaCruz
 */
public interface Compute extends Remote {

  /**
   * Executes the given task.
   * 
   * @param <T> The data type of the result of this task.
   * @param t The task to execute.
   * @return The results of the given task.
   * @throws RemoteException If a communication or protocol error has occurred.
   */
  <T> T executeTask(Task<T> t) throws RemoteException;
}
