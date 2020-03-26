Week 5 review comments:

`class Star`

when writing a if block please remember to include curly braces as it helps with readability. The below should be:
``` java
  if(hipparcosId <0) throw new IllegalArgumentException();
        else this.hipparcosId = hipparcosId;
```


``` java
  if(hipparcosId <0) {throw new IllegalArgumentException();}
        else {this.hipparcosId = hipparcosId;}
```