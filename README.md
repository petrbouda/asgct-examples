# AsyncGetCallTrace / Virtual Threads / and more..

#### Generate a temporary file
```
java src/main/java/pbouda/asgct/CreateJunkFile.java
```

#### Start generating VirtualThreads

```
$ java -XX:+UseParallelGC -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints --enable-preview --source 19 src/main/java/pbouda/asgct/VirtualThreadStacks.java
```

```
$ profiler.sh -d 30 -e cpu -i 1ms -I 'java/*' -f virtual-threads.html $(pgrep -f VirtualThreadStacks)
```

[Virtual Threads - Flamegraph](virtual-threads.html)

```
profiler.sh -d 30 -e cpu -t -i 1ms -I 'java/*' -f virtual-threads-with-t-option.html $(pgrep -f VirtualThreadStacks)
```

[Virtual Threads per-thread view - Flamegraph](virtual-threads-with-t-option.html)

```
profiler.sh -d 30 -e cpu -t -i 1ms -I 'java/*' -f virtual-threads-leveled.html $(pgrep -f LeveledVirtualThread)
```

- a Virtual Thread calls another Virtual threads inside its function

[Virtual Threads leveled - Flamegraph](virtual-threads-leveled.html)

#### JCMD `VirtualThreadStacks` Thread.print
```
$ jcmd $(pgrep -f VirtualThreadStacks) Thread.print

...
"ForkJoinPool-1-worker-1" #21 [236851] daemon prio=5 os_prio=0 cpu=53,63ms elapsed=13,03s tid=0x00007fa3782dd4d0  [0x00007fa33f4f2000]
   Carrying virtual thread #2975
	at jdk.internal.vm.Continuation.run(java.base@19/Continuation.java:257)
	at java.lang.VirtualThread.runContinuation(java.base@19/VirtualThread.java:213)
	at java.lang.VirtualThread$$Lambda$24/0x0000000801053ee8.run(java.base@19/Unknown Source)
	at java.util.concurrent.ForkJoinTask$RunnableExecuteAction.exec(java.base@19/ForkJoinTask.java:1423)
	at java.util.concurrent.ForkJoinTask.doExec$$$capture(java.base@19/ForkJoinTask.java:387)
	at java.util.concurrent.ForkJoinTask.doExec(java.base@19/ForkJoinTask.java)
	at java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(java.base@19/ForkJoinPool.java:1311)
	at java.util.concurrent.ForkJoinPool.scan(java.base@19/ForkJoinPool.java:1840)
	at java.util.concurrent.ForkJoinPool.runWorker(java.base@19/ForkJoinPool.java:1806)
	at java.util.concurrent.ForkJoinWorkerThread.run(java.base@19/ForkJoinWorkerThread.java:177)
...
```

#### JCMD `InfiniteSingleVirtualThread` Thread.print 

```
"ForkJoinPool-1-worker-1" #24 [240035] daemon prio=5 os_prio=0 cpu=31188,53ms elapsed=31,57s tid=0x00007f7eb81f71d0  [0x00007f7e7ab05000]
   Carrying virtual thread #23
	at jdk.internal.vm.Continuation.run(java.base@19/Continuation.java:257)
	at java.lang.VirtualThread.runContinuation(java.base@19/VirtualThread.java:213)
	at java.lang.VirtualThread$$Lambda$32/0x000000080105d300.run(java.base@19/Unknown Source)
	at java.util.concurrent.ForkJoinTask$RunnableExecuteAction.exec(java.base@19/ForkJoinTask.java:1423)
	at java.util.concurrent.ForkJoinTask.doExec(java.base@19/ForkJoinTask.java:387)
	at java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(java.base@19/ForkJoinPool.java:1311)
	at java.util.concurrent.ForkJoinPool.scan(java.base@19/ForkJoinPool.java:1840)
	at java.util.concurrent.ForkJoinPool.runWorker(java.base@19/ForkJoinPool.java:1806)
	at java.util.concurrent.ForkJoinWorkerThread.run(java.base@19/ForkJoinWorkerThread.java:177)

```

#### Throw an exception in the `blocking method`
```
java.lang.RuntimeException
	at pbouda.asgct.VirtualThreadStacks.doSomeBlocking(VirtualThreadStacks.java:32)
	at pbouda.asgct.VirtualThreadStacks.lambda$spawnVirtualThread$0(VirtualThreadStacks.java:26)
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:577)
	at java.base/java.util.concurrent.ThreadPerTaskExecutor$ThreadBoundFuture.run(ThreadPerTaskExecutor.java:352)
	at java.base/java.lang.VirtualThread.run(VirtualThread.java:287)
	at java.base/java.lang.VirtualThread$VThreadContinuation.lambda$new$0(VirtualThread.java:174)
	at java.base/jdk.internal.vm.Continuation.enter0(Continuation.java:327)
	at java.base/jdk.internal.vm.Continuation.enter(Continuation.java:320)
```

#### (an interesting note) unparker thread
```
"VirtualThread-unparker" #233 [238237] daemon prio=5 os_prio=0 cpu=182,46ms elapsed=4,34s tid=0x00007f284f817600 nid=238237 runnable  [0x00007f284dffd000]
   java.lang.Thread.State: RUNNABLE
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.siftDown(java.base@19/ScheduledThreadPoolExecutor.java:998)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.finishPoll(java.base@19/ScheduledThreadPoolExecutor.java:1145)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(java.base@19/ScheduledThreadPoolExecutor.java:1174)
	at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(java.base@19/ScheduledThreadPoolExecutor.java:899)
	at java.util.concurrent.ThreadPoolExecutor.getTask(java.base@19/ThreadPoolExecutor.java:1070)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(java.base@19/ThreadPoolExecutor.java:1130)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(java.base@19/ThreadPoolExecutor.java:642)
	at java.lang.Thread.run(java.base@19/Thread.java:1589)
	at jdk.internal.misc.InnocuousThread.run(java.base@19/InnocuousThread.java:186)
```