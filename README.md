<div align="center">
    <h1 id="crossbundlelib">CrossBundleLib</h1>
    <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
    <img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
    <img src="https://img.shields.io/badge/gradle-salad?style=for-the-badge&logo=gradle" alt="Gradle" />
</div>

---

**CrossBundleLib (a.k.a. Bundle Lib)** is a tiny PaperMC library that backports the 1.20+ bundle packet feature to the older
versions. It uses the selective simplified Nagle's algorithm to join needed packets together and send them as a single TCP packet.
This guarantees that client receives all the needed packets at once and handles them as close as possible. Library supports multiple
Minecraft versions via adapters and Gradle multi-project builds.

**Now the library is in a state of proofing the concept! Feel free to provide any tests!**

| Minecraft version | BundleLib adapter                  |
|-------------------|------------------------------------|
| 1.20.x            | :x: (WORK IN PROGRESS)             |
| 1.17.x            | :white_check_mark: bundlelib-v1_17 |

* [Abstract](#crossbundlelib)
* [Installation](#installation)
* [Using API](#using-api)
* [What problems does it solve?](#what-problems-does-it-solve)
* [Core concept](#core-concept)
* [Licensing and contributing](#licensing-and-contributing)
* [Special thanks](#special-thanks)

## Installation

```kotlin
repositories {
    // ...
    maven("https://jitpack.io/")
}

dependencies {
    implementation("com.github.drupaldoesnotexists.crossbundlelib:bundlelib-core:<VERSION>")
    implementation("com.github.drupaldoesnotexists.crossbundlelib:<ADAPTER>:<VERSION>")
}
```

## Using API

As BundleLib uses a custom version-, adapter- and, therefore,
platform-compliant wrapper around bundles, sometimes it requires to
inject a custom Netty encoder, so it could delegate to a custom bundle writing mechanism.

Injection can be done only once per channel, so developers have to find a way to synchronize with
other plugins. Check out [CrossBundleLib Injector](https://github.com/DrupalDoesNotExists/crossbundlelib-injector) that serves as a
PaperMC plugin and does injecting by himself.

Do note that the Player channel is unreachable during the `PlayerLoginEvent`,
so You have to schedule the task with scheduler or use some later
events like `PlayerJoinEvent`.

```java
private final CrossBundleLib lib = new CrossBundleLib(
        new BundleLibAdapter17() /* new BundleLibAdapter20(), ... */
);

@EventHandler
public void onLogin(PlayerLoginEvent event) {
    new BukkitRunnable() {
        @Override
        public void run() {
            lib.inject(event.getPlayer());
        }
    }.runTaskLaterAsynchronously(plugin, 20L);
}

// Or use a PlayerJoinEvent, it really depends on Your problem...

@EventHandler
public void onJoin(PlayerJoinEvent event) {
    lib.inject(event.getPlayer());
}

// And you can freely create a bundle!

Bundle<Packet<?>> myBundle = lib.createBundle(packet1, packet2, packet3);
// fakePlayer.send(myBundle.asPacket()); <-- Send if you need to
```

## What problems does it solve

Originally,
this library solves the [issue #40 (russian)](https://github.com/Slomix/ParkourBeat/issues/40) which describes usage of
such a library for implementing a ping-aware audio player with fine-grained server-side control over the music.

There are bugs that could be fixed using bundles,
such as entity spawn and entity equipment
being two different packets on the old versions,
so this creates a flickering on the entities with equipment.

## Core concept

This section mainly describes the replication mechanism on the older versions.
On the 1.20.x and newer native adapter
falls back to the NMS bundles implementation and native packets instead of backporting.

As you all know, Minecraft networking is built on top of Netty.
Packets are handled by specific listeners called `ChannelHandler`'s in a specific order defined by a `ChannelPipeline`.
E.g., `PacketDecoder` is responsible for converting the `ByteBuffer` to a POJO, `PacketEncoder` is doing a reverse job.

But `PacketDecoder` actually can't handle more than a single packet in a buffer at a time.
So in every version there is a channel handler called *splitter*
(class names are different among versions, e.g. `VarInt21FrameDecoder` in 1.17.1).

It grabs a large byte buffer of all the incoming bytes
and splits it by reading the length field of every single incoming packet, so only the small single-packet buffers are
passed to the packet decoder standing right next.
As splitters are present in every version starting (probably even sooner) from 1.7 to the newest versions,
"Nagled" packets are perfectly supported and could be understood by the client easily.

So to replicate the bundle behavior, BundleLib joins bundled packets to the single TCP packet
(that's a simplified Nagle's algorithm, a.k.a. Nagling).
As a client receives all the needed packets at once, the latency between handling them is minimal.

## Licensing and contributing

CrossBundleLib is available under the [MIT license](LICENSE).
Any kind of contributions is welcome.
Feel free to open an issue, discussion or PR.

## Special thanks

* @Dymeth for the amazing talks and ideas about enhancing the core concept.
* [wiki.vg Protocol docs](https://wiki.vg/Protocol) for well-written Protocol specification.
* @jacobo-mc for publishing the [Minecraft 1.18 decompiled remapped sources](https://github.com/jacobo-mc/mc_1.18.1_src/) on GitHub for reference.
