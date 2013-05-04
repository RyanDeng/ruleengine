package cn.dc.core;

import java.util.List;

public interface RuleBase {
	WorkingMemory newWorkingMemory();

    

    List<RulePackage> getPackages();

    RulePackage getPackage(String name);

    void addPackages(List<RulePackage> pkgs );

    void addPackage(RulePackage pkg);

    /**
     * This locks the current RuleBase and all there referenced StatefulSessions. This should be
     * used when there is a number of dynamic RuleBase changes you wish to make, but cannot have any normal
     * WorkingMemory operations occuring inbetween.
     */
    void lock();

    /**
     * Unlocks the RuleBase and all of the referenced StatefulSessions.
     */
    void unlock();

    /**
     * Returns the number of additive operations applied since the last lock() was obtained
     * @return
     */
    int getAdditionsSinceLock();

    /**
     * Returns the number of removal operations applied since the last lock() was obtained
     * @return
     */
    int getRemovalsSinceLock();

    /**
     * Remove the package and all it's rules, functions etc
     * @param packageName
     */
    void removePackage(String packageName);

    /**
     * Remove a specific Rule in a Package
     * @param packageName
     * @param ruleName
     */
    void removeRule(String packageName,
                    String ruleName);
    
    /**
     * Remove a specific Query in a Package
     * @param packageName
     * @param ruleName
     */
    void removeQuery(String packageName,
                     String queryName);

    /**
     * Removes a specific function in a specific package.
     * @param packageName
     * @param functionName
     */
    void removeFunction(String packageName,
                        String functionName);

    /**
     * Removes a process by the process' id
     * @param id
     */
    void removeProcess(String id);

    /**
     * Returns an array of all the referenced StatefulSessions
     * @return
     */
    public WorkingMemory[] getWorkingMemories();

   
}
