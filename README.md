# ⭐⭐⭐ StarLang ⭐⭐⭐

Beyond five stars!

## What is it?

This is THE Star language. Done, next question.

## No, seriously

Ok, fine. So this is a star-themed esolang, fitting in the two-dimentional languages category, with two major goals:

- Stars everywhere
- Code written in StarLang must always be beautiful

## Core concepts

Again, this is all about stars. It started being about starfishes, but after researching a bit I concluded that starfishes are honestly vey uninteresting, so I decided to go with stars in general. In the end, everything is mixed, so your first exercise in StarLang is to make sense of it.

- Code is organized in stars, each star has a name that is a single character, and yes, you can use unicode characters, I actually recommend you use fancy emojis
- Each star has 5 legs, north is teh one going up, then go clockwise to the east, southeast, southwest, and west.
- Stars have a state, which is an integer value representing how much light they are emitting; a zero just means they are completely off, which I believe a real star can't do, but I am not a physicist... Maybe is an eclipse or something
- Stars live very far away from each other, so they can't interact much with each other, all they can see is if another star is lit or not, and they can poke other stars; again, not a physicist, don't ask me how
- Also, stars are balls of fire, or just spongy creatures that can't move. Either way, none of those have brains, so they are dumb, they can't really do much
  - They can increase or decrease their intensity by one
  - They can do something repeatedly while themselves or some other star is lit
  - They can also do something repeatedly while themselves have at least some intensity
  - They can read one character from the standard input and make their light match that character, or print their value as a matching character (no, I could not think of a metaphor here, hard to do so when talking about things that don't do anything and just exist)
- Every time a star is pocked, only one of their legs moves, which depends on how much light they are emitting. Ah ah, star dumb or something
- If a star leg is done executing, it just goes again, unless explicitly stopped


## Enough jokes, on to the nerdy stuff!!

### Syntax

At the core of this language we have dialects, which is essentially a map of characters to tokens. I am going tofocus on the beautiful dialect, because I want you to be confused from the get-go. 

Every instruction is a single character. Instructions are either baked in or the name of a star.

- ⭐ is the center of a star, it just tells the tokenizer there's a star there. Could have gone with just finding characters which have legs coming out of them, but I am lazy and this looks much better.
- 🔵 increments the light intensity by one
- 🟠 decrements the light intensity by one. There is no safeguard, if you try to go below zero, it will explode and yell profanities in gibberish form. You are on your own
- 🛑 says that the star is done executing. It's a stop sign, not a red ball.
- 💫 begins a control, meaning, the following code will be executed repeatedly as long as some star is lit
  - if followed by a star name, that is the star that is checked
  - if followed by something else, the current star is teh reference
- 🔁 begins a control division. Basically there is an integer division on the star value by 5, if the result is at least 1, the control code executes  
  - This only works on the current star, so you cannot reference another star
  - It only exists to make math easier, cause now you can treat a star as having 10 legs. Don't make sense? yeah, I know... 
- 🏁 ends a control, code following it, is outside the control. 
  - This is optional, if left open, the end of the current star leg is the end of the control
- 📝 prints out the current star value. The integer value is converted into codepoints and the unicode representation is printed out. If it happens to be an unprintable character or something that will burst your computer in flames, to bad.
- 🔎 reads one unicode character from the standard input and sets the current star value to that character.
- ⚫ is just an empty space. Feel free to just use actual white space, but your program will look ugly, because unicode character width and whatnot.
- 🌟 is just padding, again, we want our code to be beautiful, meaning we want our cute little stars to be symmetric, and we don't want those pesky stars with shorter legs. So if you don't have anything to do on a leg, just pad it.
- Any other character results in poking the star with that name, if it exists. Otherwise, an error is raised. Except
  - When declaring a star
  - The first character of a control, in which case it just means which star to check for litness (yeah, not a word, but you get it)

## Program layout

There is a main star, representing the entrypoint of a program. On the beautiful dialect that star is a fish, 🐠, so when you are declaring the main star, you use a star ⭐ and a fish 🐠... a starfish! I know, I am hilarious with my puns. 

A Star is declared using the star character and it's name placed directly underneath. Then each leg goes straight out of it in the north, east, southeast, southwest, and west directions. Instructions flow from star center to the outer space, or deep ocean, depends on your favourite metaphor.

### Example

```
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫🌟🛑📝🟠🟠🟠🟠🟠🟠🟠🟠🟠⭐🟠🟠🟠🟠🟠🟠🌟🌟🌟🌟🌟🌟
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🐠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫
```

Beautiful, right? Plenty more on the `beautiful-examples` folder.

## Execution flow

When the program starts, it's dark, so every star is unlit, uncool, and depressed. Or maybe their value is just zero. The program starts from the main star.

Every time a star needs to be executed, which leg runs is determined by its current value. Starting with the leg facing upwards, north leg, and rotating clockwise, the leg executed is the modulus division of the current value by 5. This means that the program always starts with the north leg of the main star.

Once a leg is done running, if not explicitly done with 🛑, it starts again by deciding which leg to run according to the above rules.

Poking a star moves the execution to that star, but once that other star is done executing, the execution continues from the star that poked it. 

### Looking at the above examples

The above example simply prints an exclamation mark, it's the first printable character in the ascii table (33), and subsequently in the unicode table. This means it's easy to be lazy and brute force a script in star lang to print it.

- Execution starts in the north leg, from the center outwards. In this example is just 🟠 repeated 6 times, the star value is incremented by 1, 6 times.
- Now the north star is done, time to decide the next star, 6 % 5 = 1, so the east leg is executed
- The east leg is again just the 🟠 6 times (plus some padding), the star value is now 12, 12 % 5 = 2, next star is southeast. 
- This repeats two more times until we get to the west star, and a value of 24.
- So now we increment it by 9 more to get 33, print it with 📝, and we are done 🛑.
- Because this is the last star in the stack, the program ends.

See below how to try this out.

### TODO, look at more examples

## Running it

So you made it this far and are still determined to try it out? You brave soul...

If you are on a mac with m1 chip, you can use the pre compiled binary in the bin folder. If you are using something else, lucky you, you get to learn some actually useful skills. Just google around until you have a running sbt installation, and then run `sbt nativeLink` in the root folder. Or just `sbt run` directly. Or just cheat, install the intellij scala plugin, and open this thing there, intellij will do it all for you. Or ask chat gpt. Or give up and become a star fishing person.

There are a few examples in the `beautiful-examples` folder. Also on the examples folder, but those are just the same programs and are ugly. Some examples.

### `echo.sea` 

Prints whatever you gave it back to you, for example

```shell
$ bin/starlang run beautiful-examples/echo.sea sup
sup
```

You can also use input piped from another program, for example

```shell
$ echo "shooting star 🌠" | bin/starlang run beautiful-examples/echo.sea
shooting star 🌠
```

### reverse.sea 

Reverses the input.

```shell
$ bin/starlang run beautiful-examples/reverse.sea drawer diaper
repaid reward
```

And even reverse the reversed

```shell
bin/starlang run beautiful-examples/reverse.sea I probably make no sense reversed | bin/starlang run beautiful-examples/reverse.sea
I probably make no sense reversed
```

### rot 13

We all now the incredibly secure cypher rot13, right? You can now protect your darkest secrets with StarLang.

```shell
$ bin/starlang run beautiful-examples/rot13.sea mypassword
zlcnffjbeq
```

Note, if you want to encrypt it multiple times to be on the safe side, just make sure its a odd number of times. The result when you encrypt it an even number of times is not very secure. 


### TODO, explain how to write and test programs without going insane