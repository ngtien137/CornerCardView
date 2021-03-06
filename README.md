# CornerCardView

## Preview 
![alt text](https://github.com/ngtien137/CornerCardView/blob/master/images/preview.png) 
## Getting Started 
* This library use Kotlin 
* Still have a problem with anti aliasing
* Add maven in your root build.gradle at the end of repositories:

```gradle 
allprojects { 
  repositories { 
    ... 
    maven { url 'https://jitpack.io' }
  } 
} 
``` 

* Add the dependency to file build.gradle(Module:app): 

```gradle
implementation 'com.github.ngtien137:CornerCardView:Tag' 

``` 

* TAG is the version of library. If you don't know, remove it with + 

* News version:
```gradle
  implementation 'com.github.ngtien137:CornerCardView:1.5'
  implementation 'com.github.ngtien137:CornerCardView:1.3.oval'
  implementation 'com.github.ngtien137:CornerCardView:1.5.flex'
```
* Or you can get version of this module [here](https://jitpack.io/#ngtien137/CornerCardView)
## All Attributes 
```gradle 
  <com.lhd.views.cornercard.CornerCard
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:layout_margin="4dp"
    app:cc_corner_bottom_left="0dp"
    app:cc_corner_bottom_right="40dp"
    app:cc_corner_top_left="40dp"
    app:cc_corner_top_right="0dp"
    app:cc_background_color="#f00"
    app:cc_shadow_dx="2dp"
    app:cc_shadow_dy="-2dp"
    app:cc_shadow_radius="2dp"
    app:cc_shadow_color="#000">
  </com.lhd.views.cornercard.CornerCard>
``` 
