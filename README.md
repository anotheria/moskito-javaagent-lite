# moskito-javaagent-lite
MoSKito Javaagent Light, a quick start of MoSKito Monitoring in your application.


### 1 Build and enable.

  a). Get javaagent-lite-1.0.0-SNAPSHOT.jar  artifact from {javaagent-home}/target - directory  and   put it to  some location
  b). Get {javaagent-home}/target/appdata  directory to same location ( provides all required configurations and will be used as bootPath for agent)

### 2 Add  moskito javaagent-lite to your app, as  java-agent
   	as example:
			export JAVA_OPTS=" $JAVA_OPTS -javaagent:/[full_path]/javaagent/target/javaagent-lite-1.0.0-SNAPSHOT.jar"

    
   By default port "-1" will be used for RMIRegistry start.. ( "-1" means  fetch first free  in range [9250 - 9299] to use  - or any other preset with 
   properties ['localRmiRegistryMinPort' - 'localRmiRegistryMaxPort'] - if set) 
   - You will be able to find real Registry port in Logs:
    	[main] INFO   o.m.j.LightTransformationAgent:83 - Starting Moskito backend on using -1 port! !
    	....
    	[main] INFO   o.m.j.LightTransformationAgent:86 - Starting Moskito backend on 10000 port! Performed successfully!
 
### 2.1 Port pre-selection/re-define by property

   Property 'localRmiRegistryPort'. Should provide positive int value.
   Simply add -DlocalRmiRegistryPort=[port value] - and if it's not used - backend will be started on it.
    
    
### 3 Opts examples:    
    
    
    
   a) JAVA_OPTS="$JAVA_OPTS -javaagent:/[full_path]/javaagent-lite-1.0.0-SNAPSHOT.jar -DlocalRmiRegistryPort11=11000 " - start on 11000 if free ( error otherwise )
   
   b) JAVA_OPTS="$JAVA_OPTS -javaagent:/[full_path]/javaagent-lite-1.0.0-SNAPSHOT.jar -DlocalRmiRegistryMinPort=50000 -DlocalRmiRegistryMaxPort=51000" - start on first free in range
    [50000-51000]
    
   c) JAVA_OPTS="$JAVA_OPTS -javaagent:/[full_path]/javaagent-lite-1.0.0-SNAPSHOT.jar"  start on first free port range [9250 - 9299].


Enjoy…