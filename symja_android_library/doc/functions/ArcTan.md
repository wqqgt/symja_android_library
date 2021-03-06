## ArcTan

```
ArcTan(expr)
```

> returns the arc tangent (inverse tangent) of `expr` (measured in radians).
 
`ArcTan(expr)` will evaluate automatically in the cases `Infinity, -Infinity, 0, 1, -1`.

### Examples

```
>> ArcTan(1)    
Pi/4    
 
>> ArcTan(1.0)    
0.7853981633974483    
 
>> ArcTan(-1.0)    
-0.7853981633974483
 
>> ArcTan(1, 1)    
Pi/4   
 
>> ArcTan(-1, 1)    
3/4*Pi  
 
>> ArcTan(1, -1)    
-Pi/4  
 
>> ArcTan(-1, -1)    
-3/4*Pi    
 
>> ArcTan(1, 0)    
0    
 
>> ArcTan(-1, 0)    
Pi    
 
>> ArcTan(0, 1)    
Pi/2    
 
>> ArcTan(0, -1)    
-Pi/2   
``` 