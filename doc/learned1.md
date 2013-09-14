# Learned: Iterating over ArrayList vs ArrayDeque vs Array

## Summary

I found some interesting performance differences while solving the [timeseries-chalenge](https://github.com/cinovo/timeseries-challenge).
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
* I expected that iterating over an ArrayList, ArrayDeque or Array depends on the number of elements in the data strucutre
* I expected that iterating over an ArrayList and ArrayDeque is similar
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
			<td>11</td>
			<td>16</td>
			<td>80</td>
			<td>656 *</td>
			<td>6023 *</td>
			<td></td>
		</tr>
		<tr>
			<td>Learned1_ArrayListGet</td>
			<td>6</td>
			<td>12</td>
			<td>77</td>
			<td>520 *</td>
			<td>5504 *</td>
			<td></td>
		</tr>
		<tr>
			<td>Learned1_ArrayDequeIterator</td>
			<td>10</td>
			<td>16</td>
			<td>92</td>
			<td>759 *</td>
			<td>6276 *</td>
			<td></td>
		</tr>
		<tr>
			<td>Learned1_IntegerArray</td>
			<td>6</td>
			<td>11</td>
			<td>46</td>
			<td>456 *</td>
			<td>4375 *</td>
			<td></td>
		</tr>
		<tr>
			<td>Learned1_intArray</td>
			<td>6</td>
			<td>6</td>
			<td>10</td>
			<td>13</td>
			<td>58</td>
			<td></td>
		</tr>
	</tbody>
</table>
`````
* young generatin GC happened
`````

## Conclusions

* I expected that iterating over an ArrayList, ArrayDeque or Array depends on the number of elements in the data strucutre
	* yes: Iterating over an ArrayList and ArrayDeque grows linear to the number of elements
* I expected that iterating over an ArrayList and ArrayDeque is similar
	* no: Iterating over an ArrayList is twice as fast as iterating over an ArrayDeque
* I expected that iterating over a primitive Array int[] is faster than Integer[]
	* no: primitive Arrays are not faster than object arrays.

## Log

### Output: 1 Elements
`````
Learned1_ArrayListIterator
0.149: [GC [PSYoungGen: 15728K->384K(917504K)] 15728K->384K(1966080K), 0.0104200 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
0.160: [Full GC (System) [PSYoungGen: 384K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 384K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0163550 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.282: [GC [PSYoungGen: 94372K->64K(917504K)] 94670K->370K(1966080K), 0.0005150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.282: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 306K->295K(1048576K)] 370K->295K(1966080K) [PSPermGen: 2666K->2666K(21248K)], 0.0082930 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 11298000
run (ms): 11
Learned1_ArrayListGet
0.304: [GC [PSYoungGen: 31457K->64K(917504K)] 31752K->359K(1966080K), 0.0005880 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.304: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 295K->292K(1048576K)] 359K->292K(1966080K) [PSPermGen: 2670K->2670K(21248K)], 0.0101950 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.356: [GC [PSYoungGen: 31457K->32K(917504K)] 31749K->324K(1966080K), 0.0005870 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.356: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 324K->292K(1966080K) [PSPermGen: 2672K->2672K(21248K)], 0.0071880 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 6441000
run (ms): 6
Learned1_ArrayDequeIterator
0.372: [GC [PSYoungGen: 15728K->64K(917504K)] 16020K->356K(1966080K), 0.0006600 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
0.373: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 356K->292K(1966080K) [PSPermGen: 2677K->2677K(21248K)], 0.0065070 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.449: [GC [PSYoungGen: 62914K->32K(917504K)] 63207K->324K(1966080K), 0.0009460 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.450: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 292K->293K(1048576K)] 324K->293K(1966080K) [PSPermGen: 2685K->2685K(21248K)], 0.0081990 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 10086000
run (ms): 10
Learned1_IntegerArray
0.472: [GC [PSYoungGen: 15728K->64K(917504K)] 16021K->357K(1966080K), 0.0048710 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
0.477: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 357K->293K(1966080K) [PSPermGen: 2689K->2689K(21248K)], 0.0084610 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
0.542: [GC [PSYoungGen: 31457K->32K(917504K)] 31751K->325K(1966080K), 0.0020970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.545: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 325K->293K(1966080K) [PSPermGen: 2690K->2690K(21248K)], 0.0180860 secs] [Times: user=0.01 sys=0.00, real=0.02 secs] 
measure...
run (ns): 6401000
run (ms): 6
Learned1_intArray
0.571: [GC [PSYoungGen: 15728K->64K(917504K)] 16022K->357K(1966080K), 0.0019030 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.573: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->294K(1048576K)] 357K->294K(1966080K) [PSPermGen: 2694K->2694K(21248K)], 0.0217550 secs] [Times: user=0.02 sys=0.00, real=0.02 secs] 
start...
test true
warmup...
warmup done
0.656: [GC [PSYoungGen: 15728K->32K(917504K)] 16022K->326K(1966080K), 0.0012280 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
0.657: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 294K->294K(1048576K)] 326K->294K(1966080K) [PSPermGen: 2695K->2695K(21248K)], 0.0089770 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 6143000
run (ms): 6
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x0000000151f10000, 0x0000000191f10000, 0x0000000191f10000)
  eden space 786432K, 4% used [0x0000000151f10000,0x0000000153dc8630,0x0000000181f10000)
  from space 131072K, 0% used [0x0000000189f10000,0x0000000189f10000,0x0000000191f10000)
  to   space 131072K, 0% used [0x0000000181f10000,0x0000000181f10000,0x0000000189f10000)
 ParOldGen       total 1048576K, used 294K [0x0000000111f10000, 0x0000000151f10000, 0x0000000151f10000)
  object space 1048576K, 0% used [0x0000000111f10000,0x0000000111f59820,0x0000000151f10000)
 PSPermGen       total 21248K, used 2703K [0x000000010cd10000, 0x000000010e1d0000, 0x0000000111f10000)
  object space 21248K, 12% used [0x000000010cd10000,0x000000010cfb3c18,0x000000010e1d0000)

`````

### Output: 10 Elements
`````
Learned1_ArrayListIterator
0.151: [GC [PSYoungGen: 15728K->368K(917504K)] 15728K->368K(1966080K), 0.0103690 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
0.162: [Full GC (System) [PSYoungGen: 368K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 368K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0112290 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.466: [GC [PSYoungGen: 235929K->64K(917504K)] 236228K->370K(1966080K), 0.0007950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.467: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 306K->295K(1048576K)] 370K->295K(1966080K) [PSPermGen: 2666K->2666K(21248K)], 0.0082740 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 16020000
run (ms): 16
Learned1_ArrayListGet
0.493: [GC [PSYoungGen: 47186K->64K(917504K)] 47481K->359K(1966080K), 0.0007760 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.494: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 295K->292K(1048576K)] 359K->292K(1966080K) [PSPermGen: 2670K->2670K(21248K)], 0.0077460 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.611: [GC [PSYoungGen: 173015K->32K(917504K)] 173307K->324K(1966080K), 0.0004680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.612: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 324K->292K(1966080K) [PSPermGen: 2672K->2672K(21248K)], 0.0094220 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 12840000
run (ms): 12
Learned1_ArrayDequeIterator
0.636: [GC [PSYoungGen: 31457K->64K(917504K)] 31749K->356K(1966080K), 0.0007030 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.637: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 356K->292K(1966080K) [PSPermGen: 2677K->2677K(21248K)], 0.0105030 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.807: [GC [PSYoungGen: 188743K->32K(917504K)] 189036K->324K(1966080K), 0.0004920 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.808: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 292K->293K(1048576K)] 324K->293K(1966080K) [PSPermGen: 2684K->2684K(21248K)], 0.0066540 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 16714000
run (ms): 16
Learned1_IntegerArray
0.833: [GC [PSYoungGen: 31457K->64K(917504K)] 31750K->357K(1966080K), 0.0009180 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.834: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 357K->293K(1966080K) [PSPermGen: 2688K->2688K(21248K)], 0.0070200 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
0.910: [GC [PSYoungGen: 173015K->32K(917504K)] 173308K->325K(1966080K), 0.0004140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.910: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 293K->293K(1048576K)] 325K->293K(1966080K) [PSPermGen: 2690K->2690K(21248K)], 0.0069340 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 11143000
run (ms): 11
Learned1_intArray
0.930: [GC [PSYoungGen: 31457K->64K(917504K)] 31751K->357K(1966080K), 0.0018010 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.932: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 293K->294K(1048576K)] 357K->294K(1966080K) [PSPermGen: 2693K->2693K(21248K)], 0.0057100 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
0.951: [GC [PSYoungGen: 15728K->32K(917504K)] 16022K->326K(1966080K), 0.0004710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.952: [Full GC (System) [PSYoungGen: 32K->0K(917504K)] [ParOldGen: 294K->294K(1048576K)] 326K->294K(1966080K) [PSPermGen: 2695K->2695K(21248K)], 0.0059180 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 6661000
run (ms): 6
all done
Heap
 PSYoungGen      total 917504K, used 31457K [0x000000014ad80000, 0x000000018ad80000, 0x000000018ad80000)
  eden space 786432K, 4% used [0x000000014ad80000,0x000000014cc38630,0x000000017ad80000)
  from space 131072K, 0% used [0x0000000182d80000,0x0000000182d80000,0x000000018ad80000)
  to   space 131072K, 0% used [0x000000017ad80000,0x000000017ad80000,0x0000000182d80000)
 ParOldGen       total 1048576K, used 294K [0x000000010ad80000, 0x000000014ad80000, 0x000000014ad80000)
  object space 1048576K, 0% used [0x000000010ad80000,0x000000010adc9840,0x000000014ad80000)
 PSPermGen       total 21248K, used 2702K [0x0000000105b80000, 0x0000000107040000, 0x000000010ad80000)
  object space 21248K, 12% used [0x0000000105b80000,0x0000000105e23aa8,0x0000000107040000)

`````

### Output: 100 Elements
`````
Learned1_ArrayListIterator
0.149: [GC [PSYoungGen: 15728K->384K(917504K)] 15728K->384K(1966080K), 0.0105040 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
0.160: [Full GC (System) [PSYoungGen: 384K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 384K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0095300 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
1.103: [GC [PSYoungGen: 786432K->64K(917504K)] 786730K->362K(1966080K), 0.0006900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
1.442: [GC [PSYoungGen: 786496K->64K(917504K)] 786794K->362K(1966080K), 0.0010570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
1.457: [GC [PSYoungGen: 46953K->64K(917504K)] 47252K->362K(1966080K), 0.0004410 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
1.457: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 298K->295K(1048576K)] 362K->295K(1966080K) [PSPermGen: 2666K->2666K(21248K)], 0.0078200 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 80826000
run (ms): 80
Learned1_ArrayListGet
1.548: [GC [PSYoungGen: 179950K->96K(917504K)] 180245K->391K(1966080K), 0.0017160 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
1.550: [Full GC (System) [PSYoungGen: 96K->0K(917504K)] [ParOldGen: 295K->292K(1048576K)] 391K->292K(1966080K) [PSPermGen: 2670K->2670K(21248K)], 0.0080270 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
1.876: [GC [PSYoungGen: 786432K->32K(917504K)] 786724K->324K(1966080K), 0.0006480 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2.184: [GC [PSYoungGen: 786464K->64K(917504K)] 786756K->356K(1966080K), 0.0010350 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
2.187: [GC [PSYoungGen: 15803K->64K(917504K)] 16095K->356K(1966080K), 0.0006110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2.188: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 356K->292K(1966080K) [PSPermGen: 2672K->2672K(21248K)], 0.0103140 secs] [Times: user=0.02 sys=0.01, real=0.01 secs] 
measure...
run (ns): 77956000
run (ms): 77
Learned1_ArrayDequeIterator
2.279: [GC [PSYoungGen: 173132K->64K(917504K)] 173424K->356K(1966080K), 0.0035480 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
2.283: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->292K(1048576K)] 356K->292K(1966080K) [PSPermGen: 2677K->2677K(21248K)], 0.0244930 secs] [Times: user=0.01 sys=0.00, real=0.02 secs] 
start...
test true
warmup...
2.696: [GC [PSYoungGen: 786432K->32K(1048256K)] 786724K->324K(2096832K), 0.0009990 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
3.001: [GC [PSYoungGen: 807699K->64K(917504K)] 807992K->356K(1966080K), 0.0009900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3.002: [Full GC (System) [PSYoungGen: 64K->0K(917504K)] [ParOldGen: 292K->293K(1048576K)] 356K->293K(1966080K) [PSPermGen: 2685K->2685K(21248K)], 0.0069420 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
measure...
run (ns): 92147000
run (ms): 92
Learned1_IntegerArray
3.103: [GC [PSYoungGen: 165165K->96K(1048256K)] 165459K->389K(2096832K), 0.0010400 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
3.104: [Full GC (System) [PSYoungGen: 96K->0K(1048256K)] [ParOldGen: 293K->293K(1048576K)] 389K->293K(2096832K) [PSPermGen: 2689K->2689K(21248K)], 0.0063520 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
3.592: [GC [PSYoungGen: 1047936K->32K(1048256K)] 1048229K->325K(2096832K), 0.0007110 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
warmup done
3.769: [GC [PSYoungGen: 545197K->32K(1048320K)] 545491K->325K(2096896K), 0.0004540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3.770: [Full GC (System) [PSYoungGen: 32K->0K(1048320K)] [ParOldGen: 293K->293K(1048576K)] 325K->293K(2096896K) [PSPermGen: 2690K->2690K(21248K)], 0.0056990 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 46390000
run (ms): 46
Learned1_intArray
3.823: [GC [PSYoungGen: 167738K->96K(1048320K)] 168032K->389K(2096896K), 0.0004680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3.824: [Full GC (System) [PSYoungGen: 96K->0K(1048320K)] [ParOldGen: 293K->294K(1048576K)] 389K->294K(2096896K) [PSPermGen: 2694K->2694K(21248K)], 0.0098720 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
warmup done
3.844: [GC [PSYoungGen: 20967K->32K(1048320K)] 21261K->326K(2096896K), 0.0004610 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3.845: [Full GC (System) [PSYoungGen: 32K->0K(1048320K)] [ParOldGen: 294K->294K(1048576K)] 326K->294K(2096896K) [PSPermGen: 2695K->2695K(21248K)], 0.0061820 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
run (ns): 10560000
run (ms): 10
all done
Heap
 PSYoungGen      total 1048320K, used 41928K [0x000000014cc80000, 0x000000018cc80000, 0x000000018cc80000)
  eden space 1048064K, 4% used [0x000000014cc80000,0x000000014f5722f0,0x000000018cc00000)
  from space 256K, 0% used [0x000000018cc40000,0x000000018cc40000,0x000000018cc80000)
  to   space 256K, 0% used [0x000000018cc00000,0x000000018cc00000,0x000000018cc40000)
 ParOldGen       total 1048576K, used 294K [0x000000010cc80000, 0x000000014cc80000, 0x000000014cc80000)
  object space 1048576K, 0% used [0x000000010cc80000,0x000000010ccc99a8,0x000000014cc80000)
 PSPermGen       total 21248K, used 2703K [0x0000000107a80000, 0x0000000108f40000, 0x000000010cc80000)
  object space 21248K, 12% used [0x0000000107a80000,0x0000000107d23c18,0x0000000108f40000)

`````

### Output: 1000 Elements
`````
Learned1_ArrayListIterator
0.137: [GC [PSYoungGen: 15728K->352K(917504K)] 15728K->352K(1966080K), 0.0035860 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
0.140: [Full GC (System) [PSYoungGen: 352K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 352K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0200540 secs] [Times: user=0.02 sys=0.01, real=0.02 secs] 
start...
test true
warmup...
1.106: [GC [PSYoungGen: 786432K->64K(917504K)] 786730K->362K(1966080K), 0.0011510 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
1.473: [GC [PSYoungGen: 786496K->64K(917504K)] 786794K->362K(1966080K), 0.0006480 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
1.804: [GC [PSYoungGen: 786496K->64K(917504K)] 786794K->362K(1966080K), 0.0007070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2.199: [GC [PSYoungGen: 786496K->64K(917504K)] 786794K->362K(1966080K), 0.0006260 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
2.581: [GC [PSYoungGen: 786496K->96K(1048256K)] 786794K->402K(2096832K), 0.0006520 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3.039: [GC [PSYoungGen: 917280K->96K(917504K)] 917586K->410K(1966080K), 0.0003690 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3.413: [GC [PSYoungGen: 917280K->0K(1048256K)] 917594K->354K(2096832K), 0.0010750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3.978: [GC [PSYoungGen: 1047936K->0K(1048256K)] 1048290K->354K(2096832K), 0.0013190 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.451: [GC [PSYoungGen: 1047936K->0K(1048256K)] 1048290K->354K(2096832K), 0.0012140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
4.955: [GC [PSYoungGen: 1047936K->0K(1048256K)] 1048290K->354K(2096832K), 0.0023830 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
5.437: [GC [PSYoungGen: 1047936K->0K(1048256K)] 1048290K->354K(2096832K), 0.0006300 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
5.868: [GC [PSYoungGen: 1047936K->0K(1048256K)] 1048290K->354K(2096832K), 0.0009870 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
6.326: [GC [PSYoungGen: 1047936K->0K(1048320K)] 1048290K->354K(2096896K), 0.0010420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
6.810: [GC [PSYoungGen: 1048000K->0K(1048256K)] 1048354K->354K(2096832K), 0.0003890 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
7.234: [GC [PSYoungGen: 1048000K->0K(1048320K)] 1048354K->354K(2096896K), 0.0006140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
7.706: [GC [PSYoungGen: 1048064K->0K(1048320K)] 1048418K->354K(2096896K), 0.0011430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
7.923: [GC [PSYoungGen: 482435K->0K(1048320K)] 482789K->354K(2096896K), 0.0004520 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
7.924: [Full GC (System) [PSYoungGen: 0K->0K(1048320K)] [ParOldGen: 354K->312K(1048576K)] 354K->312K(2096896K) [PSPermGen: 2666K->2666K(21248K)], 0.0073570 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
measure...
8.348: [GC [PSYoungGen: 1048064K->0K(1048320K)] 1048376K->312K(2096896K), 0.0006250 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
run (ns): 656059000
run (ms): 656
Learned1_ArrayListGet
8.589: [GC [PSYoungGen: 545361K->64K(1048384K)] 545674K->376K(2096960K), 0.0005770 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
8.590: [Full GC (System) [PSYoungGen: 64K->0K(1048384K)] [ParOldGen: 312K->292K(1048576K)] 376K->292K(2096960K) [PSPermGen: 2670K->2670K(21248K)], 0.0081220 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
8.992: [GC [PSYoungGen: 1048128K->64K(1048320K)] 1048420K->356K(2096896K), 0.0006790 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
9.394: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048484K->316K(2096960K), 0.0006120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
9.784: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048508K->316K(2096960K), 0.0010400 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
10.180: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048508K->316K(2096960K), 0.0007680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
10.602: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048508K->316K(2096960K), 0.0006720 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
10.990: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048508K->316K(2096960K), 0.0006340 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
11.431: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048508K->316K(2096960K), 0.0003760 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
11.819: [GC [PSYoungGen: 1048192K->0K(1048448K)] 1048508K->316K(2097024K), 0.0006550 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
12.197: [GC [PSYoungGen: 1048256K->0K(1048384K)] 1048572K->316K(2096960K), 0.0005710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
12.577: [GC [PSYoungGen: 1048256K->0K(1048448K)] 1048572K->316K(2097024K), 0.0010350 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
12.956: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048636K->316K(2097024K), 0.0006350 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
13.361: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048636K->316K(2097024K), 0.0012610 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
13.745: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048636K->316K(2097024K), 0.0007150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
14.171: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048636K->316K(2097024K), 0.0005960 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
14.553: [GC [PSYoungGen: 985852K->0K(1048448K)] 986168K->316K(2097024K), 0.0005060 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
14.553: [Full GC (System) [PSYoungGen: 0K->0K(1048448K)] [ParOldGen: 316K->309K(1048576K)] 316K->309K(2097024K) [PSPermGen: 2672K->2672K(21248K)], 0.0060120 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
measure...
14.903: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048629K->309K(2097024K), 0.0004610 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
run (ns): 520469000
run (ms): 520
Learned1_ArrayDequeIterator
15.081: [GC [PSYoungGen: 524309K->64K(1048448K)] 524618K->373K(2097024K), 0.0007260 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
15.082: [Full GC (System) [PSYoungGen: 64K->0K(1048448K)] [ParOldGen: 309K->292K(1048576K)] 373K->292K(2097024K) [PSPermGen: 2677K->2677K(21248K)], 0.0062690 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
15.525: [GC [PSYoungGen: 1048320K->64K(1048448K)] 1048612K->356K(2097024K), 0.0010450 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
15.959: [GC [PSYoungGen: 1048384K->0K(1048448K)] 1048676K->324K(2097024K), 0.0003780 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
16.378: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048644K->324K(2097024K), 0.0010720 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
16.833: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048644K->324K(2097024K), 0.0006330 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
17.241: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048644K->324K(2097024K), 0.0007110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
17.662: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048644K->324K(2097024K), 0.0011150 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
18.078: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048644K->324K(2097024K), 0.0011490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
18.575: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048644K->324K(2097024K), 0.0007150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
18.998: [GC [PSYoungGen: 1048320K->0K(1048512K)] 1048644K->324K(2097088K), 0.0011440 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
19.449: [GC [PSYoungGen: 1048384K->0K(1048448K)] 1048708K->324K(2097024K), 0.0013260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
19.959: [GC [PSYoungGen: 1048384K->0K(1048512K)] 1048708K->324K(2097088K), 0.0006550 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
20.442: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048772K->324K(2097088K), 0.0019610 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
20.927: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048772K->324K(2097088K), 0.0007270 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
21.404: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048772K->324K(2097088K), 0.0006070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
21.906: [GC [PSYoungGen: 964750K->0K(1048512K)] 965075K->324K(2097088K), 0.0008010 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
21.907: [Full GC (System) [PSYoungGen: 0K->0K(1048512K)] [ParOldGen: 324K->310K(1048576K)] 324K->310K(2097088K) [PSPermGen: 2685K->2685K(21248K)], 0.0129930 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
22.418: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048758K->310K(2097088K), 0.0006460 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
run (ns): 759560000
run (ms): 759
Learned1_IntegerArray
22.682: [GC [PSYoungGen: 524286K->64K(1048512K)] 524597K->382K(2097088K), 0.0096420 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
22.692: [Full GC (System) [PSYoungGen: 64K->0K(1048512K)] [ParOldGen: 318K->293K(1048576K)] 382K->293K(2097088K) [PSPermGen: 2689K->2689K(21248K)], 0.0087610 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
23.039: [GC [PSYoungGen: 1048448K->32K(1048512K)] 1048741K->325K(2097088K), 0.0013120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
23.358: [GC [PSYoungGen: 1048480K->0K(1048512K)] 1048773K->317K(2097088K), 0.0011060 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
23.710: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0011650 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
24.031: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0005910 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
24.353: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0005470 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
24.683: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0006190 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
25.010: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0005840 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
25.332: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0009150 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
25.655: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0024730 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
25.993: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0005740 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
26.325: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0010120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
26.702: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0031280 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
27.005: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0006000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
27.298: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048765K->317K(2097088K), 0.0010240 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
27.567: [GC [PSYoungGen: 964647K->0K(1048512K)] 964965K->317K(2097088K), 0.0004710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
27.567: [Full GC (System) [PSYoungGen: 0K->0K(1048512K)] [ParOldGen: 317K->311K(1048576K)] 317K->311K(2097088K) [PSPermGen: 2690K->2690K(21248K)], 0.0062030 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
27.888: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048759K->311K(2097088K), 0.0011670 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
run (ns): 456645000
run (ms): 456
Learned1_intArray
28.033: [GC [PSYoungGen: 524250K->64K(1048512K)] 524561K->375K(2097088K), 0.0010610 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
28.034: [Full GC (System) [PSYoungGen: 64K->0K(1048512K)] [ParOldGen: 311K->294K(1048576K)] 375K->294K(2097088K) [PSPermGen: 2694K->2694K(21248K)], 0.0072110 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
28.057: [GC [PSYoungGen: 20973K->32K(1048512K)] 21267K->326K(2097088K), 0.0004870 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
28.058: [Full GC (System) [PSYoungGen: 32K->0K(1048512K)] [ParOldGen: 294K->297K(1048576K)] 326K->297K(2097088K) [PSPermGen: 2695K->2695K(21248K)], 0.0087410 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
run (ns): 13155000
run (ms): 13
all done
Heap
 PSYoungGen      total 1048512K, used 41938K [0x0000000156100000, 0x0000000196100000, 0x0000000196100000)
  eden space 1048448K, 4% used [0x0000000156100000,0x00000001589f4b38,0x00000001960e0000)
  from space 64K, 0% used [0x00000001960f0000,0x00000001960f0000,0x0000000196100000)
  to   space 64K, 0% used [0x00000001960e0000,0x00000001960e0000,0x00000001960f0000)
 ParOldGen       total 1048576K, used 297K [0x0000000116100000, 0x0000000156100000, 0x0000000156100000)
  object space 1048576K, 0% used [0x0000000116100000,0x000000011614a7b8,0x0000000156100000)
 PSPermGen       total 21248K, used 2703K [0x0000000110f00000, 0x00000001123c0000, 0x0000000116100000)
  object space 21248K, 12% used [0x0000000110f00000,0x00000001111a3c20,0x00000001123c0000)

`````

### Output: 10000 Elements
`````
Learned1_ArrayListIterator
0.161: [GC [PSYoungGen: 15728K->416K(917504K)] 15728K->416K(1966080K), 0.0023800 secs] [Times: user=0.01 sys=0.01, real=0.01 secs] 
0.163: [Full GC (System) [PSYoungGen: 416K->0K(917504K)] [ParOldGen: 0K->298K(1048576K)] 416K->298K(1966080K) [PSPermGen: 2653K->2651K(21248K)], 0.0169160 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
1.182: [GC [PSYoungGen: 786432K->231K(917504K)] 786730K->529K(1966080K), 0.0016870 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
1.609: [GC [PSYoungGen: 786663K->263K(917504K)] 786961K->561K(1966080K), 0.0017490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
2.112: [GC [PSYoungGen: 786695K->295K(917504K)] 786993K->593K(1966080K), 0.0022740 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
2.519: [GC [PSYoungGen: 786727K->231K(917504K)] 787025K->529K(1966080K), 0.0053560 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
2.960: [GC [PSYoungGen: 786663K->231K(1048128K)] 786961K->529K(2096704K), 0.0009640 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
3.518: [GC [PSYoungGen: 917287K->231K(917504K)] 917585K->529K(1966080K), 0.0012810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
3.967: [GC [PSYoungGen: 917287K->32K(1048128K)] 917585K->545K(2096704K), 0.0015380 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
4.597: [GC [PSYoungGen: 1047712K->0K(1047744K)] 1048225K->521K(2096320K), 0.0010490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
5.076: [GC [PSYoungGen: 1047680K->0K(1048064K)] 1048201K->521K(2096640K), 0.0004850 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
5.564: [GC [PSYoungGen: 1047552K->0K(1048064K)] 1048073K->521K(2096640K), 0.0009950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
6.071: [GC [PSYoungGen: 1047552K->0K(1048064K)] 1048073K->521K(2096640K), 0.0010350 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
6.528: [GC [PSYoungGen: 1047552K->0K(1048064K)] 1048073K->521K(2096640K), 0.0005130 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
7.049: [GC [PSYoungGen: 1047552K->0K(1048064K)] 1048073K->521K(2096640K), 0.0011780 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
7.481: [GC [PSYoungGen: 1047552K->0K(1048064K)] 1048073K->521K(2096640K), 0.0011610 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
7.939: [GC [PSYoungGen: 1047552K->0K(1048128K)] 1048073K->521K(2096704K), 0.0008800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
8.388: [GC [PSYoungGen: 1047680K->0K(1048128K)] 1048201K->521K(2096704K), 0.0014120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
8.833: [GC [PSYoungGen: 1047680K->0K(1048128K)] 1048201K->521K(2096704K), 0.0004610 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
9.303: [GC [PSYoungGen: 1047680K->0K(1048128K)] 1048201K->521K(2096704K), 0.0006520 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
9.744: [GC [PSYoungGen: 1047680K->0K(1048192K)] 1048201K->521K(2096768K), 0.0010380 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
10.289: [GC [PSYoungGen: 1047808K->0K(1048192K)] 1048329K->521K(2096768K), 0.0007180 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
10.722: [GC [PSYoungGen: 1047808K->0K(1048192K)] 1048329K->521K(2096768K), 0.0009480 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
11.171: [GC [PSYoungGen: 1047808K->0K(1048192K)] 1048329K->521K(2096768K), 0.0007490 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
11.613: [GC [PSYoungGen: 1047808K->0K(1048256K)] 1048329K->521K(2096832K), 0.0003420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
12.077: [GC [PSYoungGen: 1047936K->0K(1048256K)] 1048457K->521K(2096832K), 0.0006090 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
12.496: [GC [PSYoungGen: 1047936K->0K(1048256K)] 1048457K->521K(2096832K), 0.0010750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
12.987: [GC [PSYoungGen: 1047936K->0K(1048256K)] 1048457K->521K(2096832K), 0.0036660 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
13.439: [GC [PSYoungGen: 1047936K->0K(1048320K)] 1048457K->521K(2096896K), 0.0010820 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
13.900: [GC [PSYoungGen: 1048064K->0K(1048320K)] 1048585K->521K(2096896K), 0.0012000 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
14.379: [GC [PSYoungGen: 1048064K->0K(1048320K)] 1048585K->521K(2096896K), 0.0010370 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
14.846: [GC [PSYoungGen: 1048064K->0K(1048320K)] 1048585K->521K(2096896K), 0.0010520 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
15.323: [GC [PSYoungGen: 1048064K->0K(1048384K)] 1048585K->521K(2096960K), 0.0012190 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
15.839: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048713K->521K(2096960K), 0.0011810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
16.304: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048713K->521K(2096960K), 0.0005760 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
16.765: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048713K->521K(2096960K), 0.0006550 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
17.229: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048713K->521K(2096960K), 0.0010510 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
17.682: [GC [PSYoungGen: 1048192K->0K(1048384K)] 1048713K->521K(2096960K), 0.0005800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
18.158: [GC [PSYoungGen: 1048192K->0K(1048448K)] 1048713K->521K(2097024K), 0.0006360 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
18.598: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048841K->521K(2097024K), 0.0010670 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
19.074: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048841K->521K(2097024K), 0.0006600 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
19.543: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048841K->521K(2097024K), 0.0008960 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
20.034: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048841K->521K(2097024K), 0.0010110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
20.463: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048841K->521K(2097024K), 0.0011200 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
20.968: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048841K->521K(2097024K), 0.0012250 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
21.430: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048841K->521K(2097024K), 0.0005610 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
21.932: [GC [PSYoungGen: 1048320K->0K(1048512K)] 1048841K->521K(2097088K), 0.0018950 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
22.436: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005830 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
22.924: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0029890 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
23.370: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0028490 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
23.817: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
24.311: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0003600 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
24.778: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
25.325: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0007760 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
25.785: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0011620 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
26.268: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0003090 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
26.719: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
27.163: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010410 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
27.589: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010650 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
28.109: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010980 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
28.567: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006920 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
29.054: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0009820 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
29.512: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0004430 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
30.003: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0007840 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
30.456: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0004080 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
30.948: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
31.413: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005510 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
31.845: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006460 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
32.298: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006030 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
32.727: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0009710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
33.191: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010850 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
33.631: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005820 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
34.111: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010970 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
34.547: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010980 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
34.986: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010470 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
35.430: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0082740 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
35.885: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006520 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
36.326: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005890 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
36.776: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0009920 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
37.222: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005480 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
37.682: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0020300 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
38.177: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010170 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
38.604: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0007470 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
39.069: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010410 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
39.545: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0012130 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
40.130: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006450 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
40.581: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
41.090: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0014870 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
41.588: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
42.099: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
42.541: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0007720 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
43.063: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0009690 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
43.490: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005980 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
43.960: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0013150 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
44.389: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006380 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
44.824: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0007090 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
45.293: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0004620 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
45.751: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0007140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
46.265: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
46.696: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
47.154: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005360 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
47.606: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0009700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
48.084: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006410 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
48.512: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0003180 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
48.977: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010180 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
49.447: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010600 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
49.917: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010320 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
50.362: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006480 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
50.817: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005670 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
51.263: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0012190 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
51.720: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0081210 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
52.242: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006370 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
52.678: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0003370 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
53.149: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0014400 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
53.586: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
54.066: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006400 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
54.514: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0037580 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
54.978: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0003680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
55.447: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006130 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
55.932: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0012580 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
56.384: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005920 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
56.836: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006350 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
57.296: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
57.729: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0039630 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
58.245: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0022110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
58.672: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0003490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
59.146: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010010 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
59.583: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0009950 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
60.046: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010320 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
60.481: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0009710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
60.938: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0042160 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
61.429: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005620 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
61.902: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0023270 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
62.369: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006640 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
62.838: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0011510 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
63.368: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006320 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
63.838: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006790 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
64.397: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0003800 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
64.871: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0075600 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
65.357: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005390 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
65.832: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
66.325: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006300 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
66.804: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0006700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
67.354: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0008560 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
67.800: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
68.286: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010880 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
68.776: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010240 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
69.314: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0025290 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
69.834: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0005050 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
70.380: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0003170 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
70.845: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0004530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
71.330: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048969K->521K(2097088K), 0.0010460 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
warmup done
71.617: [GC [PSYoungGen: 629493K->0K(1048512K)] 630014K->521K(2097088K), 0.0009780 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
71.618: [Full GC (System) [PSYoungGen: 0K->0K(1048512K)] [ParOldGen: 521K->488K(1048576K)] 521K->488K(2097088K) [PSPermGen: 2667K->2667K(21248K)], 0.0109920 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
72.139: [GC [PSYoungGen: 1048448K->32K(1048512K)] 1048936K->520K(2097088K), 0.0005700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
72.597: [GC [PSYoungGen: 1048480K->0K(1048512K)] 1048968K->488K(2097088K), 0.0006660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
73.010: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0005830 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
73.446: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0006910 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
73.828: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0005970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
74.240: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0005730 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
74.620: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0010450 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
74.984: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0090940 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
75.363: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0006120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
75.747: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0011260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
76.144: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0005630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
76.515: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0006900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
76.914: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0010990 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
77.293: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048936K->488K(2097088K), 0.0010060 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
run (ns): 6023201000
run (ms): 6023
Learned1_ArrayListGet
77.660: [GC [PSYoungGen: 964994K->64K(1048512K)] 965483K->552K(2097088K), 0.0030810 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
77.663: [Full GC (System) [PSYoungGen: 64K->0K(1048512K)] [ParOldGen: 488K->292K(1048576K)] 552K->292K(2097088K) [PSPermGen: 2670K->2670K(21248K)], 0.0173120 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
78.116: [GC [PSYoungGen: 1048448K->64K(1048384K)] 1048740K->499K(2096960K), 0.0009960 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
78.489: [GC [PSYoungGen: 1048384K->0K(1048448K)] 1048819K->507K(2097024K), 0.0011220 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
78.916: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048827K->507K(2097024K), 0.0008790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
79.333: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048827K->507K(2097024K), 0.0010310 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
79.733: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048827K->507K(2097024K), 0.0012270 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
80.157: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048827K->507K(2097024K), 0.0005950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
80.535: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048827K->507K(2097024K), 0.0038880 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
80.932: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048827K->507K(2097024K), 0.0006070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
81.336: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048827K->507K(2097024K), 0.0006860 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
81.789: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048827K->507K(2097024K), 0.0003490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
82.250: [GC [PSYoungGen: 1048320K->0K(1048512K)] 1048827K->507K(2097088K), 0.0006430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
82.625: [GC [PSYoungGen: 1048384K->0K(1048448K)] 1048891K->507K(2097024K), 0.0005450 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
83.063: [GC [PSYoungGen: 1048384K->0K(1048512K)] 1048891K->507K(2097088K), 0.0011140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
83.468: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
83.850: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010390 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
84.290: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0017270 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
84.681: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006050 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
85.104: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011160 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
85.529: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0012030 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
85.966: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010290 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
86.360: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0007110 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
86.764: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011670 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
87.167: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
87.563: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005720 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
87.971: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006550 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
88.414: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
88.807: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
89.227: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005840 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
89.640: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005980 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
90.062: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006550 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
90.446: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011440 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
90.841: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009850 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
91.253: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003550 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
91.682: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
92.106: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
92.510: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005980 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
92.912: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
93.305: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
93.749: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010610 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
94.197: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003010 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
94.582: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006400 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
94.999: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0007200 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
95.383: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
95.774: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006410 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
96.216: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011180 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
96.640: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011030 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
97.085: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
97.516: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006020 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
97.961: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
98.355: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010620 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
98.745: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009960 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
99.190: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009360 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
99.595: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0007240 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
100.017: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0004330 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
100.398: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
100.810: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
101.222: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006420 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
101.633: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
102.120: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005940 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
102.517: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010390 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
102.931: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0012910 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
103.383: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
103.773: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006300 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
104.172: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
104.557: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005880 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
104.950: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006830 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
105.333: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009760 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
105.739: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006050 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
106.126: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010160 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
106.552: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0112860 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
107.008: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
107.394: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0017190 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
107.787: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006130 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
108.242: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005940 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
108.660: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009730 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
109.133: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0012140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
109.521: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
109.962: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
110.356: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006080 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
110.763: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
111.174: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
111.583: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
112.019: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005560 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
112.456: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006960 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
112.833: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003590 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
113.251: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003860 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
113.641: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010140 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
114.075: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005610 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
114.467: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006310 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
114.854: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
115.318: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
115.735: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005820 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
116.153: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005870 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
116.550: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009750 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
116.937: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010100 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
117.328: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003370 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
117.772: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006760 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
118.165: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
118.618: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0012530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
119.046: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0007350 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
119.446: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006860 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
119.835: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005980 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
120.285: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006080 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
120.692: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
121.123: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003590 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
121.570: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006330 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
122.000: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006110 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
122.391: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
122.793: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
123.218: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009960 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
123.624: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010950 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
124.075: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011770 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
124.476: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010800 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
124.884: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010170 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
125.284: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
125.672: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005600 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
126.083: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
126.491: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005390 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
126.929: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
127.348: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0110530 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
127.744: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009850 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
128.176: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009970 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
128.576: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011830 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
128.994: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006730 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
129.379: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006280 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
129.771: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0003260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
130.187: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009940 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
130.629: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0012390 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
131.040: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
131.438: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005440 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
131.824: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005740 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
132.257: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006470 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
132.641: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
133.015: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005550 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
133.406: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006560 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
133.873: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0012180 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
134.261: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
134.640: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006200 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
135.046: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0017500 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
135.428: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0009980 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
135.854: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0012440 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
136.317: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
136.703: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0011440 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
137.124: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0005790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
137.511: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0078440 secs] [Times: user=0.03 sys=0.00, real=0.00 secs] 
137.937: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0007430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
138.323: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
138.711: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0010070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
139.123: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048955K->507K(2097088K), 0.0006390 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
139.139: [GC [PSYoungGen: 62924K->0K(1048512K)] 63432K->507K(2097088K), 0.0038890 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
139.143: [Full GC (System) [PSYoungGen: 0K->0K(1048512K)] [ParOldGen: 507K->485K(1048576K)] 507K->485K(2097088K) [PSPermGen: 2673K->2673K(21248K)], 0.0093890 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
measure...
139.546: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0010740 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
139.937: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0058460 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
140.294: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0012540 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
140.659: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0006630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
141.044: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0005470 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
141.396: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0006080 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
141.762: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0006750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
142.144: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0050940 secs] [Times: user=0.02 sys=0.01, real=0.00 secs] 
142.535: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0005890 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
142.908: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0006660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
143.247: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0010410 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
143.602: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0011370 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
143.989: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0010950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
144.324: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048933K->485K(2097088K), 0.0006760 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
run (ns): 5504892000
run (ms): 5504
Learned1_ArrayDequeIterator
144.660: [GC [PSYoungGen: 964846K->64K(1048512K)] 965332K->549K(2097088K), 0.0008490 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
144.661: [Full GC (System) [PSYoungGen: 64K->0K(1048512K)] [ParOldGen: 485K->292K(1048576K)] 549K->292K(2097088K) [PSPermGen: 2678K->2678K(21248K)], 0.0062260 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
145.193: [GC [PSYoungGen: 1048448K->64K(1048512K)] 1048740K->524K(2097088K), 0.0012180 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
145.665: [GC [PSYoungGen: 1048512K->32K(1048384K)] 1048972K->572K(2096960K), 0.0008650 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
146.104: [GC [PSYoungGen: 1048352K->0K(1048448K)] 1048892K->548K(2097024K), 0.0010860 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
146.508: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0005740 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
146.948: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0006850 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
147.345: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0005840 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
147.766: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0006680 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
148.213: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0005790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
148.673: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0003300 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
149.125: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0005770 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
149.540: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0012180 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
150.000: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0005470 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
150.420: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048868K->548K(2097024K), 0.0010790 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
150.906: [GC [PSYoungGen: 1048320K->0K(1048512K)] 1048868K->548K(2097088K), 0.0010490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
151.342: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006230 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
151.839: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006050 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
152.277: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
152.687: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
153.138: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005770 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
153.569: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005170 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
154.019: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010500 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
154.483: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005990 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
154.981: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006280 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
155.404: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005280 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
155.822: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005940 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
156.277: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0012520 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
156.726: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006140 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
157.189: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0086150 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
157.666: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005400 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
158.095: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0007130 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
158.529: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006720 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
158.989: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0102280 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
159.406: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
159.827: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
160.263: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
160.776: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0012720 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
161.295: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
161.730: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0011070 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
162.216: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005870 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
162.639: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0004280 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
163.097: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0011060 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
163.554: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0013090 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
163.991: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005440 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
164.406: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
164.848: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
165.294: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0012900 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
165.722: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010370 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
166.166: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0011110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
166.632: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0008190 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
167.096: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0008650 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
167.558: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0020200 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
168.071: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006500 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
168.572: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
169.094: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005710 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
169.539: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0013840 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
170.021: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
170.449: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006610 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
170.876: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0069610 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
171.290: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006590 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
171.721: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006730 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
172.190: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006270 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
172.665: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
173.127: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0004190 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
173.561: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
174.021: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006010 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
174.427: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010690 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
174.845: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005730 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
175.281: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006060 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
175.754: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010080 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
176.184: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0019830 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
176.590: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0026780 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
177.048: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
177.477: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0007040 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
177.933: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0081850 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
178.349: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0011490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
178.801: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006820 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
179.234: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
179.647: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006230 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
180.061: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0003990 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
180.458: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
180.868: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0011000 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
181.282: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006030 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
181.742: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006870 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
182.157: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0013010 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
182.580: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0011540 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
183.053: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010890 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
183.465: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005570 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
183.934: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
184.347: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
184.820: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0008220 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
185.259: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010240 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
185.716: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0008270 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
186.153: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
186.573: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010320 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
187.042: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
187.470: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005080 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
187.906: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0009710 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
188.320: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0007280 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
188.739: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
189.183: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010410 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
189.592: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0081390 secs] [Times: user=0.03 sys=0.00, real=0.00 secs] 
190.087: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
190.482: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0003640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
190.995: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
191.418: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
191.844: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
192.282: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
192.693: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010390 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
193.158: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005830 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
193.586: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0011150 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
194.087: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010880 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
194.515: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
194.963: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006090 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
195.380: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0007020 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
195.819: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006450 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
196.315: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0013650 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
196.743: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005920 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
197.208: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005740 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
197.620: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010240 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
198.067: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005720 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
198.493: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0007120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
198.961: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005760 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
199.368: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
199.845: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005200 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
200.292: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0004900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
200.699: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010460 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
201.131: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006740 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
201.552: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0026570 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
202.025: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
202.437: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005600 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
202.900: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005940 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
203.334: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
203.754: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
204.196: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010930 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
204.609: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0009400 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
205.043: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005740 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
205.448: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0082570 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
205.959: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
206.379: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010450 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
206.799: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010160 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
207.257: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005660 secs] [Times: user=0.00 sys=0.01, real=0.00 secs] 
207.667: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005000 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
208.104: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0006880 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
208.515: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0009800 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
209.029: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0010630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
209.451: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0012110 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
209.908: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0004700 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
210.311: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0005890 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
210.729: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048996K->548K(2097088K), 0.0003260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
warmup done
210.748: [GC [PSYoungGen: 62918K->0K(1048512K)] 63467K->548K(2097088K), 0.0008810 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
210.749: [Full GC (System) [PSYoungGen: 0K->0K(1048512K)] [ParOldGen: 548K->511K(1048576K)] 548K->511K(2097088K) [PSPermGen: 2686K->2686K(21248K)], 0.0125620 secs] [Times: user=0.02 sys=0.00, real=0.02 secs] 
measure...
211.259: [GC [PSYoungGen: 1048448K->32K(1048512K)] 1048959K->543K(2097088K), 0.0005590 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
211.729: [GC [PSYoungGen: 1048480K->0K(1048512K)] 1048991K->511K(2097088K), 0.0009800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
212.155: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0010570 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
212.538: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0012890 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
212.954: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0006960 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
213.350: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0006640 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
213.778: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0007120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
214.188: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0010190 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
214.619: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0003590 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
215.060: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0010040 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
215.457: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0003540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
215.846: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0010630 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
216.245: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0006850 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
216.658: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048959K->511K(2097088K), 0.0006310 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
run (ns): 6276621000
run (ms): 6276
Learned1_IntegerArray
217.040: [GC [PSYoungGen: 964750K->64K(1048512K)] 965261K->575K(2097088K), 0.0008200 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
217.041: [Full GC (System) [PSYoungGen: 64K->0K(1048512K)] [ParOldGen: 511K->293K(1048576K)] 575K->293K(2097088K) [PSPermGen: 2690K->2690K(21248K)], 0.0055270 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
start...
test true
warmup...
217.343: [GC [PSYoungGen: 1048448K->39K(1048384K)] 1048741K->492K(2096960K), 0.0010620 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
217.670: [GC [PSYoungGen: 1048359K->0K(1048448K)] 1048812K->492K(2097024K), 0.0011000 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
217.995: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048812K->492K(2097024K), 0.0007140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
218.292: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048812K->492K(2097024K), 0.0006820 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
218.589: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048812K->492K(2097024K), 0.0003850 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
218.917: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048812K->492K(2097024K), 0.0010910 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
219.210: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048812K->492K(2097024K), 0.0010380 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
219.496: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048812K->492K(2097024K), 0.0006490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
219.795: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048812K->492K(2097024K), 0.0009440 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
220.126: [GC [PSYoungGen: 1048320K->0K(1048448K)] 1048812K->492K(2097024K), 0.0010350 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
220.418: [GC [PSYoungGen: 1048320K->0K(1048512K)] 1048812K->492K(2097088K), 0.0010470 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
220.749: [GC [PSYoungGen: 1048384K->0K(1048448K)] 1048876K->492K(2097024K), 0.0009530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
221.066: [GC [PSYoungGen: 1048384K->0K(1048512K)] 1048876K->492K(2097088K), 0.0009750 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
221.373: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
221.680: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006990 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
222.003: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0027540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
222.292: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009840 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
222.588: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
222.895: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0027400 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
223.195: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
223.494: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0102530 secs] [Times: user=0.03 sys=0.00, real=0.01 secs] 
223.825: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0018530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
224.153: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
224.435: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005920 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
224.727: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
225.055: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005740 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
225.339: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005810 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
225.629: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0007190 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
225.961: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
226.261: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010410 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
226.597: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005720 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
226.897: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009860 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
227.193: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010300 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
227.482: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005780 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
227.784: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005250 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
228.088: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010590 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
228.383: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011440 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
228.679: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
228.979: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010140 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
229.271: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0016560 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
229.584: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
229.960: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010450 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
230.258: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006220 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
230.560: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005910 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
230.858: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0008150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
231.177: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011230 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
231.486: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010940 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
231.787: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
232.122: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005080 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
232.419: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006410 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
232.751: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010540 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
233.070: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006160 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
233.364: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005800 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
233.661: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011990 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
233.986: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0016770 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
234.283: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010540 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
234.586: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005820 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
234.891: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010880 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
235.200: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006610 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
235.506: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005910 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
235.812: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005960 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
236.125: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
236.422: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005750 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
236.728: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0008650 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
237.046: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
237.343: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005300 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
237.645: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010660 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
238.009: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006450 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
238.312: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005860 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
238.604: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005380 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
238.961: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006480 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
239.258: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009600 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
239.557: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006900 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
239.848: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006220 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
240.168: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0007030 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
240.460: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010370 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
240.759: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011200 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
241.070: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010750 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
241.364: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006670 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
241.707: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011410 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
242.031: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005510 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
242.337: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009990 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
242.639: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006320 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
242.961: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005780 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
243.250: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006330 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
243.548: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
243.845: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006160 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
244.168: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005990 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
244.457: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006370 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
244.790: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006330 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
245.103: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010850 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
245.412: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006430 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
245.713: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011600 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
246.024: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
246.321: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005970 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
246.626: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0003530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
246.944: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010150 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
247.256: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005500 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
247.556: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0040190 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
247.899: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010300 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
248.185: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010100 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
248.486: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010400 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
248.773: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006130 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
249.077: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0056840 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
249.372: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010830 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
249.692: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0007770 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
250.003: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006330 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
250.296: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010220 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
250.587: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005890 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
250.936: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011500 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
251.228: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011270 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
251.545: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0013420 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
251.837: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0049670 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
252.165: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009980 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
252.461: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005590 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
252.749: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011050 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
253.058: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011110 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
253.347: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006040 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
253.651: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006240 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
254.021: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005080 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
254.320: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005910 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
254.623: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0003580 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
254.928: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0019640 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
255.229: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0003520 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
255.529: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010260 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
255.823: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005920 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
256.130: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005290 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
256.430: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0007740 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
256.764: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
257.087: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
257.381: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0007330 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
257.681: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0032460 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
257.996: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0009790 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
258.291: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006360 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
258.590: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006040 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
258.898: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0021070 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
259.212: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0006490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
259.490: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010500 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
259.793: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0011930 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
260.090: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010500 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
260.386: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010600 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
260.675: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
260.971: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0013480 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
261.265: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0010360 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
261.562: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0059700 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
261.920: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005780 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
262.209: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0003520 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
262.496: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0005950 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
262.816: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048940K->492K(2097088K), 0.0014130 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
warmup done
262.832: [GC [PSYoungGen: 62914K->0K(1048512K)] 63407K->492K(2097088K), 0.0004730 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
262.833: [Full GC (System) [PSYoungGen: 0K->0K(1048512K)] [ParOldGen: 492K->486K(1048576K)] 492K->486K(2097088K) [PSPermGen: 2692K->2692K(21248K)], 0.0111190 secs] [Times: user=0.02 sys=0.01, real=0.01 secs] 
measure...
263.162: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0009550 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
263.446: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0005460 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
263.730: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0010790 secs] [Times: user=0.01 sys=0.00, real=0.01 secs] 
264.029: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0010370 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
264.308: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0005890 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
264.594: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0005490 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
264.881: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0105710 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
265.195: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0011380 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
265.471: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0006120 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
265.758: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0009010 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
266.103: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0005550 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
266.383: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0005530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
266.669: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0011630 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
266.966: [GC [PSYoungGen: 1048448K->0K(1048512K)] 1048934K->486K(2097088K), 0.0005880 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
run (ns): 4375864000
run (ms): 4375
Learned1_intArray
267.221: [GC [PSYoungGen: 964687K->64K(1048512K)] 965174K->550K(2097088K), 0.0012530 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
267.223: [Full GC (System) [PSYoungGen: 64K->0K(1048512K)] [ParOldGen: 486K->294K(1048576K)] 550K->294K(2097088K) [PSPermGen: 2695K->2695K(21248K)], 0.0068100 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
start...
test true
warmup...
warmup done
267.288: [GC [PSYoungGen: 21009K->39K(1048512K)] 21303K->333K(2097088K), 0.0005940 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 
267.288: [Full GC (System) [PSYoungGen: 39K->0K(1048512K)] [ParOldGen: 294K->333K(1048576K)] 333K->333K(2097088K) [PSPermGen: 2696K->2696K(21248K)], 0.0105940 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
measure...
run (ns): 58356000
run (ms): 58
all done
Heap
 PSYoungGen      total 1048512K, used 41939K [0x000000014ad20000, 0x000000018ad20000, 0x000000018ad20000)
  eden space 1048448K, 4% used [0x000000014ad20000,0x000000014d614f00,0x000000018ad00000)
  from space 64K, 0% used [0x000000018ad00000,0x000000018ad00000,0x000000018ad10000)
  to   space 64K, 0% used [0x000000018ad10000,0x000000018ad10000,0x000000018ad20000)
 ParOldGen       total 1048576K, used 333K [0x000000010ad20000, 0x000000014ad20000, 0x000000014ad20000)
  object space 1048576K, 0% used [0x000000010ad20000,0x000000010ad73458,0x000000014ad20000)
 PSPermGen       total 21248K, used 2704K [0x0000000105b20000, 0x0000000106fe0000, 0x000000010ad20000)
  object space 21248K, 12% used [0x0000000105b20000,0x0000000105dc40f0,0x0000000106fe0000)

`````

### Output: 100000 Elements
`````

`````
