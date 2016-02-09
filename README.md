# ![](app/src/main/res/mipmap-mdpi/ic_launcher.png) DroidKaigi 2016 official Android app [![Circle CI](https://circleci.com/gh/konifar/droidkaigi2016/tree/master.svg?style=svg)](https://circleci.com/gh/konifar/droidkaigi2016/tree/master) [![Stories in Ready](https://badge.waffle.io/konifar/droidkaigi2016.svg?label=ready&title=Ready)](http://waffle.io/konifar/droidkaigi2016)

[DroidKaigi](https://droidkaigi.github.io/2016/en/) is a conference tailored for developers on 18th and 19th February 2016.

[<img src="https://dply.me/3r0x9j/button/large" alt="Try it on your device via DeployGate">](https://dply.me/3r0x9j#install)


## Features

* Show all sessions
* Manage schedule
* Show map
* Search sessions and speakers


## Development Environment

### Java8 & retrolambda
This project use Java8 and [retrolambda](https://github.com/orfjackal/retrolambda).
If you haven't set up Java8 yet, install it from [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html), and set env `JAVA_HOME` or `JAVA8_HOME`.

### DataBinding
This project tries to use [DataBinding](http://developer.android.com/intl/ja/tools/data-binding/guide.html).

```xml
<TextView
    android:id="@+id/txt_place"
    style="@style/Tag"
    android:layout_marginEnd="@dimen/spacing_xsmall"
    android:layout_marginRight="@dimen/spacing_xsmall"
    android:layout_marginTop="@dimen/spacing_xsmall"
    android:background="@drawable/tag_language"
    android:text="@{session.place.name}" /
```

Custom attributes are also used like below.

```xml
<ImageView
    android:id="@+id/img_speaker"
    android:layout_width="@dimen/user_image_small"
    android:layout_height="@dimen/user_image_small"
    android:layout_below="@id/tag_container"
    android:layout_marginTop="@dimen/spacing_small"
    android:contentDescription="@string/speaker"
    app:speakerImageUrl="@{session.speaker.imageUrl}" />
```

BindingAdapter like `speakerImageUrl` is written in `DataBindingAttributeUtil.java`.

```java
@BindingAdapter("speakerImageUrl")
public static void setSpeakerImageUrl(ImageView imageView, @Nullable String imageUrl) {
    if (TextUtils.isEmpty(imageUrl)) {
        imageView.setImageDrawable(ContextCompat.getDrawable(imageView.getContext(), R.drawable.ic_speaker_placeholder));
    } else {
        Picasso.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_speaker_placeholder)
                .error(R.drawable.ic_speaker_placeholder)
                .transform(new CropCircleTransformation())
                .into(imageView);
    }
}
```

### Dagger2
This project uses DI library [Dagger2](http://google.github.io/dagger/).
See classes in `di` package.

```
src/main/java/io/github/droidkaigi/confsched/di
|
|--scope
|  |--ActivityScope.java
|  |--FragmentScope.java
|
|--ActivityComponent.java
|--ActivityModule.java
|--AppComponent.java
|--AppModule.java
|--FragmentComponent.java
|--FragmentModule.java
```


### Orma
This project uses ORM library [Android-Orma](http://gfx.github.io/Android-Orma/).
Android-Orma is a lightning-fast and anotation based wrapper library of SQLiteDatabase.

Some model classes in `model` package have `@Table` annotation.

```java
@Table
public class Session {
    @Column(indexed = true)
    @SerializedName("id")
    public int id;

    @Column(indexed = true)
    @SerializedName("title")
    public String title;

    // ...
}
```

These classes are saved in database via `dao/SessionDao`.
To know more about Android-Orma, see [document](http://gfx.github.io/Android-Orma/).


## Todo
This project is under development.
Issues are managed by waffle.io.
https://waffle.io/konifar/droidkaigi2016

If you have a feature you want or find some bugs, please write an issue.


## Libraries
This project uses some modern Android libraries.

* Android Support Libraries
  * CardView
  * Design
  * RecyclerView
  * CustomTabs
* [Dagger2](http://google.github.io/dagger/) - Google
* [Retrofit2](http://square.github.io/retrofit/) - Square
* [Picasso](http://square.github.io/picasso/) - Square
* [OkHttp3](https://github.com/square/okhttp/tree/master/okhttp/src/main/java/okhttp3) - Square
* [Android-Orma](https://github.com/gfx/Android-Orma) - gfx
* [RxAndroid](https://github.com/ReactiveX/RxAndroid) - ReactiveX
* [ThreeTenABP](https://github.com/JakeWharton/ThreeTenABP) - JakeWharton
* [Stetho](http://facebook.github.io/stetho/) - Facebook


## License

```
Copyright 2016 Yusuke Konishi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
