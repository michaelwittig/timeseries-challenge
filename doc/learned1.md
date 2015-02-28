# Learned: Iterating over ArrayList vs ArrayDeque vs Array

## Summary

I found some interesting performance differences while solving the [timeseries-chalenge](https://github.com/michaelwittig/timeseries-challenge).
I assumed that iterating over an ArrayList should be comparable to iterating over an Array. But I found out that the opposite is true:
Iterating over an Array using
`````
for (int i = 0; i < array.length; i++) {} 
`````
is much faster than iterating over an ArrayList using
`````
for (Object o : arraylist) {}
`````
But actually I am very confused by this result.

## Expectation
* I expected that iterating over an ArrayList, ArrayDeque or Array depends linear on the number of elements in the data strucutre
* I expected that iterating over an ArrayList, ArrayDeque and Array of Object is similar
* I expected that iterating over a primitive Array int[] is faster than Integer[]

## Benchmark

`````
java version "1.7.0_07"
Java(TM) SE Runtime Environment (build 1.7.0_07-b10)
Java HotSpot(TM) 64-Bit Server VM (build 23.3-b01, mixed mode)
`````

* VM arguments: -Xmx2G -Xms2G -Xmn1G -verbose:gc -XX:+PrintGCTimeStamps -XX:+PrintGCDetails
* call System.gc() to remove garbage from previous run
* iterating 1 mio times over the data structure to warm up the VM.
* call System.gc() to remove garbage from warmup
* begin = System.nanoTime()
* iteration 100000 times over the data structure
* end = System.nanoTime()
* runtime = end-begin

### Result

<table>
	<thead>
		<tr>
			<th>Implementation</th>
			<th>1 Elements</th>
			<th>10 Elements</th>
			<th>100 Elements</th>
			<th>1000 Elements</th>
			<th>10000 Elements</th>
			<th>100000 Elements</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>Learned1_ArrayListIterator</td>
			<td>4</td>
			<td>8</td>
			<td>22</td>
			<td>140</td>
			<td>1087</td>
			<td>11731</td>
		</tr>
		<tr>
			<td>Learned1_ArrayListGet</td>
			<td>4</td>
			<td>5</td>
			<td>21</td>
			<td>99</td>
			<td>1058</td>
			<td>11177</td>
		</tr>
		<tr>
			<td>Learned1_ArrayDequeIterator</td>
			<td>7</td>
			<td>10</td>
			<td>26</td>
			<td>206</td>
			<td>1947</td>
			<td>19597</td>
		</tr>
		<tr>
			<td>Learned1_IntegerArray</td>
			<td>5</td>
			<td>6</td>
			<td>11</td>
			<td>47</td>
			<td>482</td>
			<td>4806</td>
		</tr>
		<tr>
			<td>Learned1_intArray *</td>
			<td>4</td>
			<td>4</td>
			<td>9</td>
			<td>12</td>
			<td>52</td>
			<td>465</td>
		</tr>
	</tbody>
</table>
`````
* the comparision is not fair because we have no conversion from Integer to int here during sum
`````

## Conclusions

* I expected that iterating over an ArrayList, ArrayDeque or Array depends linear on the number of elements in the data strucutre
	* yes: grows linear to the number of elements
* I expected that iterating over an ArrayList, ArrayDeque and Array is similar
	* no: Iterating over an ArrayList is faster than iterating over an ArrayDeque
	* no: Iterating over an Array is faster than iterating over ArrayList
* I expected that iterating over a primitive Array int[] is faster than Integer[]
	* hard to measure, I can't say something to this

## Log

### Output: 1 Elements
`````
Learned1_ArrayListIterator
0.114: [GC [PSYoungGen: 15728K->400K(917504K)] 15728K->400K(1966080K), 0.0016680 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.116: [Full GC (System) [PSYoungGen: 400K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 400K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0180300 secs] [Times: user=0.04 sys=0.00, real=0.02 secs] 
start...
test true
warmup...
warmup done
0.174: [GC [PSYoungGen: 47186K->96K(917504K)] 47484K->394K(1966080K), 0.0014760 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.175: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 298K->294K(1048576K)] 394K->294K(1966080K) [PSPermGen: 2665K->2665K(21248K)], 0.0104540 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 4775000
run (ms): 4
Learned1_ArrayListGet
0.192: [GC [PSYoungGen: 31457K->64K(917504K)] 31751K->358K(1966080K), 0.0003790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.193: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 294K->291K(1048576K)] 358K->291K(1966080K) [PSPermGen: 2669K->2669K(21248K)], 0.0062680 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.221: [GC [PSYoungGen: 15728K->96K(917504K)] 16020K->387K(1966080K), 0.0002110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.221: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 291K->292K(1048576K)] 387K->292K(1966080K) [PSPermGen: 2671K->2671K(21248K)], 0.0060080 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 4398000
run (ms): 4
Learned1_ArrayDequeIterator
0.233: [GC [PSYoungGen: 15728K->64K(917504K)] 16020K->356K(1966080K), 0.0003930 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.234: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 356K->292K(1966080K) [PSPermGen: 2676K->2676K(21248K)], 0.0069470 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.274: [GC [PSYoungGen: 15728K->64K(917504K)] 16021K->356K(1966080K), 0.0003430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.274: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->293K(1048576K)] 356K->293K(1966080K) [PSPermGen: 2683K->2683K(21248K)], 0.0058730 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 7392000
run (ms): 7
Learned1_IntegerArray
0.289: [GC [PSYoungGen: 15728K->64K(917504K)] 16021K->357K(1966080K), 0.0006860 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.290: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 357K->293K(1966080K) [PSPermGen: 2688K->2688K(21248K)], 0.0047860 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
0.304: [GC [PSYoungGen: 15728K->32K(917504K)] 16022K->325K(1966080K), 0.0003430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.305: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 325K->293K(1966080K) [PSPermGen: 2689K->2689K(21248K)], 0.0046280 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 5272000
run (ms): 5
Learned1_intArray
0.316: [GC [PSYoungGen: 15728K->64K(917504K)] 16022K->357K(1966080K), 0.0005420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.317: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->294K(1048576K)] 357K->294K(1966080K) [PSPermGen: 2692K->2692K(21248K)], 0.0061700 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.330: [GC [PSYoungGen: 15728K->32K(917504K)] 16022K->326K(1966080K), 0.0003740 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.330: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 294K->294K(1048576K)] 326K->294K(1966080K) [PSPermGen: 2694K->2694K(21248K)], 0.0059480 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 4381000
run (ms): 4
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x0000000158300000, 0x0000000198300000, 0x0000000198300000)
  eden space 786432K, 4% used [0x0000000158300000,0x000000015a1b8630,0x0000000188300000)
  from space 131072K, 0% used [0x0000000190300000,0x0000000190300000,0x0000000198300000)
  to   space 131072K, 0% used [0x0000000188300000,0x0000000188300000,0x0000000190300000)
 ParOldGen       total 1048576K, used 294K [0x0000000118300000, 0x0000000158300000, 0x0000000158300000)
  object space 1048576K, 0% used [0x0000000118300000,0x0000000118349820,0x0000000158300000)
 PSPermGen       total 21248K, used 2701K [0x0000000113100000, 0x00000001145c0000, 0x0000000118300000)
  object space 21248K, 12% used [0x0000000113100000,0x00000001133a3710,0x00000001145c0000)

`````

### Output: 10 Elements
`````
Learned1_ArrayListIterator
0.109: [GC [PSYoungGen: 15728K->384K(917504K)] 15728K->384K(1966080K), 0.0047740 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
0.114: [Full GC (System) [PSYoungGen: 384K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 384K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0119890 secs] [Times: user=0.02 sys=0.01, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.186: [GC [PSYoungGen: 47186K->96K(917504K)] 47484K->394K(1966080K), 0.0003750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.186: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 298K->295K(1048576K)] 394K->295K(1966080K) [PSPermGen: 2665K->2665K(21248K)], 0.0143130 secs] [Times: user=0.04 sys=0.00, real=0.02 secs] 
measure...
run (ns): 8528000
run (ms): 8
Learned1_ArrayListGet
0.211: [GC [PSYoungGen: 31457K->64K(917504K)] 31752K->359K(1966080K), 0.0009480 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.212: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 295K->292K(1048576K)] 359K->292K(1966080K) [PSPermGen: 2669K->2669K(21248K)], 0.0084110 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.257: [GC [PSYoungGen: 15728K->64K(917504K)] 16020K->356K(1966080K), 0.0008890 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.258: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 356K->292K(1966080K) [PSPermGen: 2671K->2671K(21248K)], 0.0134800 secs] [Times: user=0.03 sys=0.00, real=0.02 secs] 
measure...
run (ns): 5900000
run (ms): 5
Learned1_ArrayDequeIterator
0.279: [GC [PSYoungGen: 15728K->64K(917504K)] 16020K->356K(1966080K), 0.0003030 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.280: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 356K->292K(1966080K) [PSPermGen: 2676K->2676K(21248K)], 0.0041250 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.329: [GC [PSYoungGen: 15728K->64K(917504K)] 16021K->356K(1966080K), 0.0003560 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.329: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->293K(1048576K)] 356K->293K(1966080K) [PSPermGen: 2683K->2683K(21248K)], 0.0052880 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 10847000
run (ms): 10
Learned1_IntegerArray
0.347: [GC [PSYoungGen: 15728K->64K(917504K)] 16021K->357K(1966080K), 0.0005260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.347: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 357K->293K(1966080K) [PSPermGen: 2687K->2687K(21248K)], 0.0041610 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.369: [GC [PSYoungGen: 15728K->32K(917504K)] 16022K->325K(1966080K), 0.0003980 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.370: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 325K->293K(1966080K) [PSPermGen: 2689K->2689K(21248K)], 0.0099810 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 6015000
run (ms): 6
Learned1_intArray
0.387: [GC [PSYoungGen: 15728K->64K(917504K)] 16022K->357K(1966080K), 0.0003720 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.388: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->294K(1048576K)] 357K->294K(1966080K) [PSPermGen: 2692K->2692K(21248K)], 0.0061070 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.402: [GC [PSYoungGen: 15728K->32K(917504K)] 16022K->326K(1966080K), 0.0009380 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.403: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 294K->294K(1048576K)] 326K->294K(1966080K) [PSPermGen: 2693K->2693K(21248K)], 0.0055750 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 4682000
run (ms): 4
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x000000014b2b0000, 0x000000018b2b0000, 0x000000018b2b0000)
  eden space 786432K, 4% used [0x000000014b2b0000,0x000000014d168630,0x000000017b2b0000)
  from space 131072K, 0% used [0x00000001832b0000,0x00000001832b0000,0x000000018b2b0000)
  to   space 131072K, 0% used [0x000000017b2b0000,0x000000017b2b0000,0x00000001832b0000)
 ParOldGen       total 1048576K, used 294K [0x000000010b2b0000, 0x000000014b2b0000, 0x000000014b2b0000)
  object space 1048576K, 0% used [0x000000010b2b0000,0x000000010b2f9840,0x000000014b2b0000)
 PSPermGen       total 21248K, used 2701K [0x00000001060b0000, 0x0000000107570000, 0x000000010b2b0000)
  object space 21248K, 12% used [0x00000001060b0000,0x00000001063535a0,0x0000000107570000)

`````

### Output: 100 Elements
`````
Learned1_ArrayListIterator
0.124: [GC [PSYoungGen: 15728K->352K(917504K)] 15728K->352K(1966080K), 0.0023030 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.126: [Full GC (System) [PSYoungGen: 352K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 352K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0152690 secs] [Times: user=0.03 sys=0.00, real=0.02 secs] 
start...
test true
warmup...
warmup done
0.329: [GC [PSYoungGen: 47186K->96K(917504K)] 47484K->394K(1966080K), 0.0006670 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.330: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 298K->295K(1048576K)] 394K->295K(1966080K) [PSPermGen: 2665K->2665K(21248K)], 0.0069410 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 22105000
run (ms): 22
Learned1_ArrayListGet
0.361: [GC [PSYoungGen: 31457K->64K(917504K)] 31752K->359K(1966080K), 0.0005530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.361: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 295K->292K(1048576K)] 359K->292K(1966080K) [PSPermGen: 2669K->2669K(21248K)], 0.0083200 secs] [Times: user=0.02 sys=0.01, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.481: [GC [PSYoungGen: 15728K->32K(917504K)] 16020K->324K(1966080K), 0.0008200 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.482: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 324K->292K(1966080K) [PSPermGen: 2671K->2671K(21248K)], 0.0041650 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 21062000
run (ms): 21
Learned1_ArrayDequeIterator
0.509: [GC [PSYoungGen: 15728K->64K(917504K)] 16021K->356K(1966080K), 0.0004810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.510: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 356K->292K(1966080K) [PSPermGen: 2676K->2676K(21248K)], 0.0052760 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
0.743: [GC [PSYoungGen: 15728K->64K(917504K)] 16021K->356K(1966080K), 0.0002700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.743: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->293K(1048576K)] 356K->293K(1966080K) [PSPermGen: 2683K->2683K(21248K)], 0.0048410 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 26579000
run (ms): 26
Learned1_IntegerArray
0.776: [GC [PSYoungGen: 15728K->64K(917504K)] 16022K->357K(1966080K), 0.0004810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.777: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 357K->293K(1966080K) [PSPermGen: 2688K->2688K(21248K)], 0.0041410 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.837: [GC [PSYoungGen: 15729K->32K(917504K)] 16022K->325K(1966080K), 0.0003390 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.838: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 325K->293K(1966080K) [PSPermGen: 2689K->2689K(21248K)], 0.0041230 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 11905000
run (ms): 11
Learned1_intArray
0.855: [GC [PSYoungGen: 15728K->64K(917504K)] 16022K->357K(1966080K), 0.0003770 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.856: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->294K(1048576K)] 357K->294K(1966080K) [PSPermGen: 2692K->2692K(21248K)], 0.0079680 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.873: [GC [PSYoungGen: 15729K->32K(917504K)] 16023K->326K(1966080K), 0.0007090 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.873: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 294K->294K(1048576K)] 326K->294K(1966080K) [PSPermGen: 2694K->2694K(21248K)], 0.0041940 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 9008000
run (ms): 9
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x00000001580b0000, 0x00000001980b0000, 0x00000001980b0000)
  eden space 786432K, 4% used [0x00000001580b0000,0x0000000159f685e8,0x00000001880b0000)
  from space 131072K, 0% used [0x00000001900b0000,0x00000001900b0000,0x00000001980b0000)
  to   space 131072K, 0% used [0x00000001880b0000,0x00000001880b0000,0x00000001900b0000)
 ParOldGen       total 1048576K, used 294K [0x00000001180b0000, 0x00000001580b0000, 0x00000001580b0000)
  object space 1048576K, 0% used [0x00000001180b0000,0x00000001180f99a8,0x00000001580b0000)
 PSPermGen       total 21248K, used 2701K [0x0000000112eb0000, 0x0000000114370000, 0x00000001180b0000)
  object space 21248K, 12% used [0x0000000112eb0000,0x0000000113153710,0x0000000114370000)

`````

### Output: 1000 Elements
`````
Learned1_ArrayListIterator
0.108: [GC [PSYoungGen: 15728K->416K(917504K)] 15728K->416K(1966080K), 0.0018320 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
0.110: [Full GC (System) [PSYoungGen: 416K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 416K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0074080 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
1.187: [GC [PSYoungGen: 47186K->64K(917504K)] 47484K->362K(1966080K), 0.0102360 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
1.197: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 298K->312K(1048576K)] 362K->312K(1966080K) [PSPermGen: 2665K->2665K(21248K)], 0.0065300 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 140904000
run (ms): 140
Learned1_ArrayListGet
1.346: [GC [PSYoungGen: 31457K->64K(917504K)] 31770K->376K(1966080K), 0.0003570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
1.347: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 312K->292K(1048576K)] 376K->292K(1966080K) [PSPermGen: 2669K->2669K(21248K)], 0.0053540 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
2.282: [GC [PSYoungGen: 15728K->64K(917504K)] 16020K->356K(1966080K), 0.0004690 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2.283: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->309K(1048576K)] 356K->309K(1966080K) [PSPermGen: 2671K->2671K(21248K)], 0.0060610 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 99000000
run (ms): 99
Learned1_ArrayDequeIterator
2.389: [GC [PSYoungGen: 15728K->64K(917504K)] 16038K->373K(1966080K), 0.0003120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2.390: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 309K->292K(1048576K)] 373K->292K(1966080K) [PSPermGen: 2676K->2676K(21248K)], 0.0109220 secs] [Times: user=0.02 sys=0.00, real=0.02 secs] 
start...
test true
warmup...
warmup done
4.356: [GC [PSYoungGen: 15728K->96K(917504K)] 16021K->388K(1966080K), 0.0003410 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
4.356: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 292K->310K(1048576K)] 388K->310K(1966080K) [PSPermGen: 2683K->2683K(21248K)], 0.0050600 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 206071000
run (ms): 206
Learned1_IntegerArray
4.569: [GC [PSYoungGen: 15728K->64K(917504K)] 16039K->374K(1966080K), 0.0005120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.569: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 310K->293K(1048576K)] 374K->293K(1966080K) [PSPermGen: 2688K->2688K(21248K)], 0.0048790 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
5.040: [GC [PSYoungGen: 15732K->96K(917504K)] 16026K->389K(1966080K), 0.0006410 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
5.040: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 293K->311K(1048576K)] 389K->311K(1966080K) [PSPermGen: 2690K->2690K(21248K)], 0.0045690 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
measure...
run (ns): 47117000
run (ms): 47
Learned1_intArray
5.093: [GC [PSYoungGen: 15728K->64K(917504K)] 16039K->375K(1966080K), 0.0003510 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
5.094: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 311K->294K(1048576K)] 375K->294K(1966080K) [PSPermGen: 2693K->2693K(21248K)], 0.0062560 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
5.115: [GC [PSYoungGen: 15732K->32K(917504K)] 16026K->326K(1966080K), 0.0002640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
5.115: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 294K->297K(1048576K)] 326K->297K(1966080K) [PSPermGen: 2695K->2695K(21248K)], 0.0041270 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
measure...
run (ns): 12511000
run (ms): 12
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x0000000153e30000, 0x0000000193e30000, 0x0000000193e30000)
  eden space 786432K, 4% used [0x0000000153e30000,0x0000000155ce85e8,0x0000000183e30000)
  from space 131072K, 0% used [0x000000018be30000,0x000000018be30000,0x0000000193e30000)
  to   space 131072K, 0% used [0x0000000183e30000,0x0000000183e30000,0x000000018be30000)
 ParOldGen       total 1048576K, used 297K [0x0000000113e30000, 0x0000000153e30000, 0x0000000153e30000)
  object space 1048576K, 0% used [0x0000000113e30000,0x0000000113e7a7b8,0x0000000153e30000)
 PSPermGen       total 21248K, used 2702K [0x000000010ec30000, 0x00000001100f0000, 0x0000000113e30000)
  object space 21248K, 12% used [0x000000010ec30000,0x000000010eed39d8,0x00000001100f0000)

`````

### Output: 10000 Elements
`````
Learned1_ArrayListIterator
0.116: [GC [PSYoungGen: 15728K->416K(917504K)] 15728K->416K(1966080K), 0.0019760 secs] [Times: user=0.00 sys=0.01, real=0.01 secs] 
0.118: [Full GC (System) [PSYoungGen: 416K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 416K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0065310 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
11.230: [GC [PSYoungGen: 47186K->295K(917504K)] 47484K->601K(1966080K), 0.0010630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
11.231: [Full GC (System) [PSYoungGen: 295K->0K(917504K)] [ParOldGen: 306K->488K(1048576K)] 601K->488K(1966080K) [PSPermGen: 2667K->2667K(21248K)], 0.0069750 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 1087143000
run (ms): 1087
Learned1_ArrayListGet
12.327: [GC [PSYoungGen: 31457K->96K(917504K)] 31945K->584K(1966080K), 0.0003060 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
12.327: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 488K->292K(1048576K)] 584K->292K(1966080K) [PSPermGen: 2670K->2670K(21248K)], 0.0059340 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
22.983: [GC [PSYoungGen: 15728K->263K(917504K)] 16020K->555K(1966080K), 0.0006490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
22.984: [Full GC (System) [PSYoungGen: 263K->0K(917504K)] [ParOldGen: 292K->485K(1048576K)] 555K->485K(1966080K) [PSPermGen: 2673K->2673K(21248K)], 0.0056330 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 1058103000
run (ms): 1058
Learned1_ArrayDequeIterator
24.049: [GC [PSYoungGen: 15728K->96K(917504K)] 16214K->581K(1966080K), 0.0002600 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
24.050: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 485K->292K(1048576K)] 581K->292K(1966080K) [PSPermGen: 2677K->2677K(21248K)], 0.0040570 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
42.900: [GC [PSYoungGen: 15728K->320K(917504K)] 16021K->612K(1966080K), 0.0010000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
42.901: [Full GC (System) [PSYoungGen: 320K->0K(917504K)] [ParOldGen: 292K->511K(1048576K)] 612K->511K(1966080K) [PSPermGen: 2685K->2685K(21248K)], 0.0067880 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 1947565000
run (ms): 1947
Learned1_IntegerArray
44.857: [GC [PSYoungGen: 15728K->96K(917504K)] 16240K->607K(1966080K), 0.0002690 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
44.857: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 511K->293K(1048576K)] 607K->293K(1966080K) [PSPermGen: 2690K->2690K(21248K)], 0.0041550 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
49.144: [GC [PSYoungGen: 15767K->263K(917504K)] 16061K->556K(1966080K), 0.0006790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
49.145: [Full GC (System) [PSYoungGen: 263K->0K(917504K)] [ParOldGen: 293K->486K(1048576K)] 556K->486K(1966080K) [PSPermGen: 2691K->2691K(21248K)], 0.0057790 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 482627000
run (ms): 482
Learned1_intArray
49.635: [GC [PSYoungGen: 15728K->64K(917504K)] 16215K->550K(1966080K), 0.0005030 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
49.636: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 486K->294K(1048576K)] 550K->294K(1966080K) [PSPermGen: 2694K->2694K(21248K)], 0.0055450 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
49.697: [GC [PSYoungGen: 15767K->39K(917504K)] 16061K->333K(1966080K), 0.0005940 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
49.697: [Full GC (System) [PSYoungGen: 39K->0K(917504K)] [ParOldGen: 294K->333K(1048576K)] 333K->333K(1966080K) [PSPermGen: 2696K->2696K(21248K)], 0.0042210 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 52178000
run (ms): 52
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x0000000151d60000, 0x0000000191d60000, 0x0000000191d60000)
  eden space 786432K, 4% used [0x0000000151d60000,0x0000000153c185e8,0x0000000181d60000)
  from space 131072K, 0% used [0x0000000189d60000,0x0000000189d60000,0x0000000191d60000)
  to   space 131072K, 0% used [0x0000000181d60000,0x0000000181d60000,0x0000000189d60000)
 ParOldGen       total 1048576K, used 333K [0x0000000111d60000, 0x0000000151d60000, 0x0000000151d60000)
  object space 1048576K, 0% used [0x0000000111d60000,0x0000000111db3458,0x0000000151d60000)
 PSPermGen       total 21248K, used 2703K [0x000000010cb60000, 0x000000010e020000, 0x0000000111d60000)
  object space 21248K, 12% used [0x000000010cb60000,0x000000010ce03ea8,0x000000010e020000)

`````

### Output: 100000 Elements
`````
Learned1_ArrayListIterator
0.119: [GC [PSYoungGen: 15728K->416K(917504K)] 15728K->416K(1966080K), 0.0036020 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
0.123: [Full GC (System) [PSYoungGen: 416K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 416K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0122510 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
121.057: [GC [PSYoungGen: 47186K->1990K(917504K)] 47484K->2297K(1966080K), 0.0050560 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
121.062: [Full GC (System) [PSYoungGen: 1990K->0K(917504K)] [ParOldGen: 306K->2246K(1048576K)] 2297K->2246K(1966080K) [PSPermGen: 2667K->2667K(21248K)], 0.0240930 secs] [Times: user=0.06 sys=0.00, real=0.02 secs] 
measure...
run (ns): 11731091000
run (ms): 11731
Learned1_ArrayListGet
132.819: [GC [PSYoungGen: 31457K->96K(917504K)] 33703K->2342K(1966080K), 0.0002660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
132.819: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 2246K->292K(1048576K)] 2342K->292K(1966080K) [PSPermGen: 2670K->2670K(21248K)], 0.0058820 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
246.582: [GC [PSYoungGen: 15728K->2022K(917504K)] 16020K->2314K(1966080K), 0.0086610 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
246.591: [Full GC (System) [PSYoungGen: 2022K->0K(917504K)] [ParOldGen: 292K->2243K(1048576K)] 2314K->2243K(1966080K) [PSPermGen: 2673K->2673K(21248K)], 0.0295740 secs] [Times: user=0.06 sys=0.00, real=0.03 secs] 
measure...
run (ns): 11177545000
run (ms): 11177
Learned1_ArrayDequeIterator
257.799: [GC [PSYoungGen: 15728K->96K(917504K)] 17971K->2339K(1966080K), 0.0002740 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
257.799: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 2243K->292K(1048576K)] 2339K->292K(1966080K) [PSPermGen: 2677K->2677K(21248K)], 0.0041000 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
450.891: [GC [PSYoungGen: 15728K->2144K(917504K)] 16021K->2436K(1966080K), 0.0043460 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
450.896: [Full GC (System) [PSYoungGen: 2144K->0K(917504K)] [ParOldGen: 292K->2365K(1048576K)] 2436K->2365K(1966080K) [PSPermGen: 2685K->2685K(21248K)], 0.0238110 secs] [Times: user=0.06 sys=0.00, real=0.02 secs] 
measure...
run (ns): 19597868000
run (ms): 19597
Learned1_IntegerArray
470.519: [GC [PSYoungGen: 15728K->96K(917504K)] 18094K->2461K(1966080K), 0.0002640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
470.519: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 2365K->293K(1048576K)] 2461K->293K(1966080K) [PSPermGen: 2690K->2690K(21248K)], 0.0043190 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
513.187: [GC [PSYoungGen: 16119K->2022K(917504K)] 16413K->2316K(1966080K), 0.0039470 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
513.191: [Full GC (System) [PSYoungGen: 2022K->0K(917504K)] [ParOldGen: 293K->2244K(1048576K)] 2316K->2244K(1966080K) [PSPermGen: 2691K->2691K(21248K)], 0.0242960 secs] [Times: user=0.06 sys=0.00, real=0.03 secs] 
measure...
run (ns): 4806134000
run (ms): 4806
Learned1_intArray
518.023: [GC [PSYoungGen: 15728K->96K(917504K)] 17973K->2340K(1966080K), 0.0002730 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
518.023: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 2244K->294K(1048576K)] 2340K->294K(1966080K) [PSPermGen: 2694K->2694K(21248K)], 0.0047700 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
518.508: [GC [PSYoungGen: 16119K->390K(917504K)] 16413K->684K(1966080K), 0.0002700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
518.508: [Full GC (System) [PSYoungGen: 390K->0K(917504K)] [ParOldGen: 294K->684K(1048576K)] 684K->684K(1966080K) [PSPermGen: 2696K->2696K(21248K)], 0.0056070 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 465741000
run (ms): 465
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x000000014e5e0000, 0x000000018e5e0000, 0x000000018e5e0000)
  eden space 786432K, 4% used [0x000000014e5e0000,0x00000001504985e8,0x000000017e5e0000)
  from space 131072K, 0% used [0x00000001865e0000,0x00000001865e0000,0x000000018e5e0000)
  to   space 131072K, 0% used [0x000000017e5e0000,0x000000017e5e0000,0x00000001865e0000)
 ParOldGen       total 1048576K, used 684K [0x000000010e5e0000, 0x000000014e5e0000, 0x000000014e5e0000)
  object space 1048576K, 0% used [0x000000010e5e0000,0x000000010e68b2d0,0x000000014e5e0000)
 PSPermGen       total 21248K, used 2703K [0x00000001093e0000, 0x000000010a8a0000, 0x000000010e5e0000)
  object space 21248K, 12% used [0x00000001093e0000,0x0000000109683ea0,0x000000010a8a0000)

`````
