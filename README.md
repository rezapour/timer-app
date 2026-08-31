# Interval Timer

An Android workout interval timer built for running, cardio, and other training sessions where you want to focus on the workout instead of constantly checking your phone or watch.

> 🚧 **Work in progress**
>
> This app is under active development. V1 is currently being built and improved continuously, so features, architecture, and UI may change as the project evolves.

## Why I Made This App

I like building apps around problems I actually have.

I often go running, especially when preparing for longer-distance runs, and I wanted a simple timer that could guide me through an interval plan: **run, rest, repeat**, and tell me what I should be doing next.

What annoyed me was having to keep checking my phone or watch during a run. I wanted something that could keep track of the workout plan for me so I could focus on running.

I noticed the same problem at the gym, especially during cardio workouts where I want to alternate between effort and recovery periods without constantly thinking about the timer.

So I decided to build this app.

The goal is to gradually turn it into a practical workout companion that can keep track of interval-based training and make those sessions easier to follow.

**Keep up with the project — there is much more to come.**

## V1

The first version focuses on the core workout flow.

### Add a Workout

Create and configure a workout with:

- Workout name
- Work duration
- Rest duration
- Number of rounds

### My Workouts

Save your workouts and access them again whenever you need them.

From your workout list, you can select a workout and start the timer.

### Interval Workout Timer

For V1, a workout consists of two phases:

**Work → Rest → Work → Rest → ... → Finish**

For example:

```text
Work:   60 seconds
Rest:   30 seconds
Rounds: 5
```

The timer takes care of moving through the intervals and rounds so you can focus on the workout instead of watching the clock.

This model is intentionally simple for V1. The workout system will become more flexible as the app evolves.

## Tech Stack

The project is written in **Kotlin** and follows a modular Android architecture.

- Kotlin
- Jetpack Compose
- Material 3
- Navigation 3
- Coroutines & Flow
- Room
- Koin
- Kotlin Serialization
- JUnit 5
- MockK
- Turbine

The codebase is divided into `app`, `core`, and `feature` modules, with dedicated modules for areas such as the design system, domain and data layers, workouts, and the timer flow.

## Project Status

🚧 **Actively under development**

This repository is public and the app is being improved continuously.

I'm using this project both to build something useful for my own training and to explore and improve Android architecture, UI, testing, and product decisions as the app grows.

Expect frequent changes while V1 takes shape.

## Run the Project

Clone the repository:

```bash
git clone https://github.com/rezapour/timer-app.git
```

Open the project in Android Studio and run the `app` configuration on an Android device or emulator.

The current minimum Android SDK is **API 24**.

## Roadmap

The immediate goal is to finish and polish the **V1 workout experience**.

From there, I want to expand beyond the current work/rest model and make workouts more flexible, especially for running, interval training, and gym cardio.

This is only the beginning.
