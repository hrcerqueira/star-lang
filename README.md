# ⭐⭐⭐ StarLang ⭐⭐⭐

Beyond five stars!

## What is it?

This is THE Star language. Here's a picture of the StarLang council in action.
![PXL_20250508_182953233.jpg](council.jpg)
## No, seriously

Ok, fine. So this is a star-themed esolang, fitting in the two-dimentional languages category, with two major goals:

- Stars everywhere
- Code written in StarLang must always be beautiful

## Core concepts

Again, this is all about stars. It started being about starfishes, but after researching a bit I concluded that starfishes are honestly vey uninteresting, so I decided to go with stars in general. In the end, everything is mixed, so your first exercise in StarLang is to make sense of it.

- Code is organized in stars, each star has a name that is a single character, and yes, you can use unicode characters, I actually recommend you use fancy emojis
- Each star has 5 legs, north is the one going up, then go clockwise to the east, southeast, southwest, and west.
- Stars have a state, which is an integer value representing how much light they are emitting; a zero just means they are completely off, which I believe a real star can't do, but I am not a physicist... Maybe is an eclipse or something
- Stars live very far away from each other, so they can't interact much with each other, all they can see is if another star is lit or not, and they can poke other stars; again, not a physicist, don't ask me how
- Also, stars are balls of fire, or just spongy creatures that can't move. Either way, none of those have brains, so they are dumb, they can't really do much
  - They can increase or decrease their intensity by one
  - They can do something repeatedly while themselves or some other star is lit
  - They can also do something repeatedly while themselves have at least some intensity
  - They can read one character from the standard input and make their light match that character, or print their value as a matching character (no, I could not think of a metaphor here, hard to do so when talking about things that don't do anything and just exist)
- Every time a star is poked, only one of their legs moves, which depends on how much light they are emitting. Ah ah, star dumb or something
- If a star leg is done executing, it just goes again, unless explicitly stopped


## Enough jokes, on to the nerdy stuff!!

StarLang is an esolang. It is close to being turing complete, is actually what is called a turing tarpit. To actually be turing complete it would have to be memory unbound, which is not, since there's a limit (albeit a big one) in the number of stars, meaning that memory is constrained. We could also get by, by making the star values limitless, but hey, it is what it is.    

Now I haven't run a formal proof on this, I like to waste my time only up to a point, but I am fairly confident,  so my source is "trust me bro". Also, the fact that the instruction set is somehow similar to brainfuck, which has the exact same limitation.

StarLang also fits in the two-dimensional language category, meaning, code is laid out in the two-dimensional space, and flows in every direction.    



### Syntax

At the core of this language we have dialects, which is essentially a map of characters to tokens. I am going tofocus on the beautiful dialect, because I want you to be confused from the get-go. 

Every instruction is a single character. Instructions are either baked in or the name of a star.

- ⭐ is the center of a star, it just tells the tokenizer there's a star there. Could have gone with just finding characters which have legs coming out of them, but I am lazy and this looks much better.
- 🔵 increments the light intensity by one
- 🟠 decrements the light intensity by one. There is no safeguard, if you try to go below zero, it will explode and yell profanities in gibberish form. You are on your own
- 🛑 says that the star is done executing. It's a stop sign, not a red ball.
- 💫 begins a control, meaning, the following code will be executed repeatedly as long as some star is lit
  - if followed by a star name, that is the star that is checked
  - if followed by something else, the current star is the reference
- 🔁 begins a division control. Basically there is an integer division on the star value by 5, if the result is at least 1, the control code executes  
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

Star leg size must be balanced to be beautiful, but computers suck, and if you print out a star with all legs using the same amount of characters it will look weird, so after a long StarLang council meeting, we decided that the east and west legs must have twice the size of the others. Another cool thing for you to deal with.     

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

### More examples

At some point in time, in between this heading and the next one there was a beautiful explanation of the `smarter_surprise` sample, but in the end it looked like one of those multi level marketing social media posts, with more emojis than letters, so I moved the explanation below, using the alternative syntax. Oh yeah, there is one of those.  

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

### game_of_life

This is supposed to be an implementation of the conway game of life, but is not complete. In fact is barely started. Anyway, feel free to still run it and feel utterly disappointed.  

### Writing starlang programs without going insane

I am very much nit an insane person, so I did not write any of the sample programs by composing stars by hand. For that there is an alternative yucky syntax, that just looks awful, but it is easier to understand unfortunately, because we humans do not comprehend true beauty. 

The idea is, star legs are just sets of instructions, and each star has 5 of them, so we can just use boring old characters distributed in 5 lines and call it a day. We also need the star name, but that goes first. Here's the  example from above in alternative syntax.

```
$
++++++
++++++
++++++
++++++
+++++++++.!
```

Pretty bland... But the example is also stupid. Here's a list of the available instructions.

- `+` increment the star value by 1
- `-` decrement the star value by 1
- `!` finish star execution
- `[` begins control
- `{`  begins a division control
- `.` prints the value
- `?` reads a value
- any other character is star names
- no padding or special white space character
- `*` marks a star, but in this syntax is not used, only if you use these characters to produce ugly stars, which is also possible and I am not explaining it here because I don't want you to do it. 


Essentially the syntax is as follows

```
C          ; a start starts with it's name, this one is called C. It also supports comments, did I not say that? anything after a semicolon is a comment
mY+        ; each of the following lines represents a leg, this one is the north leg
fm   ##+   ; east, also, white space in betwen instructions is ignored
I+         ; souteast
[##@]+     ; southwest
[-]!       ; west

```

- A blank line (or EOF) denotes the end of a star
- You don't need to fill or balance all the legs, padding will be used when converting


#### A smarter surprise 


```
$                 
t+
tttt+
t+
m+
[tt+]-.!


m
+!
-!

t
[m-!]+
[m-!]+
[m-!]+
[m-!]+
[m-!]+!
```
Ugly but kind of neat? Here's an explanation.

Let's look at the `m` star first. Its called `m` for `mode`, you will see why in a minute.

```
m
+!
-!
```

- When this star is poked and the current value is zero, it executes the north leg, which just increments the value by one and terminates.
- When this star is poked and the current value is 1, it executes the east star, which just decrements it, flipping it back to zero. 
- Essentially this star flips between 0 and 1 every time is poked, essentially acting as a flag.

Now the `t` star. The `t` stands for thirty because we will fill it up to thirty. Fun, right?

- When this star is poked all the legs act similarly, except the west (last) one
- We have a control in the beginning, enclosed between [], the `m` means, check if `m` is lit, if yes, execute the rest of the instructions
- The instructions are, decrement the value, terminate star execution. Essentially, if `m` is 1, the `t` star decrements itself.
- Remember that controls are repeated while the condition is met, so if we didn't terminate it, we would have an infinite loop 
- If the `m` value is zero, it will skip the control, and just increment itself. 
- Now the trick here is that each leg increments its value by one but does not terminate, which means execution continues on the next leg, which will repeat the process, eventually reaching the west leg that terminates after incrementing. 
- Essentially every time `t` is poked and `m` is 0, `t` is incremented by 5

Now on to the main star, which is `$` in this dialect.

- When we start execution, everything is zero, so all three stars, m, `t` and $ are set to zero.
- The north leg in the main star is executed, it just pokes `t` once, and increments itself.
- `t` is now 5, since `m` is still 0, and $ is 1
- Now execution moves to the east leg, 1 % 5 = 0
- Here we just poke `t` another 4 times, and increment. `t` is now 25, $ is 2
- This leg is a bit longer, we're just trying to be smart with leg sizes since east and west need to be twice the size, as the others and we want to generate smaller stars. They are much cuter.
- The southeast leg is just more of the same, poke `t` once, increment. `t` is now 30, $ is 3
- The real fun begins now, are you as excited as I am?
- The northwest leg just pokes m, now `m` is 1, meaning, from now on, poking `t` will make it decrement by 1. Current start, t=30, m=1, $=4
- Finally, the west leg. The first thing we have is a control where we chck the value of t
- If `t` is not zero, we poke t, remember the first `t` in the control is the name of the star we are checking, this second `t` is us poking it.
- Then we also increment the $ star. So this control, every time it runs, `t` is decremented by 1, `$` is incremented by 1.
- Eventually `t` reaches zero and the control stops, at this point, the value of `$` is 34
- Remember that we want to print an exclamation mark, and the $ value is 34, which is a `"` and printing just that would be silly.  
- `!` codepoint is 33, so we remove the difference, which is just 1.
- We finally print it with `.` and call it a day.

Amazing right? About 50 characters to print a `!`. Lets compare it to java

```java
package io.hnrc.star.lang;

public class ExclamationMarkerPrinter {
    
    public void main(String... args) {
        System.out.println("!");
    }
}

```

I am not counting that crap. But is like 100, way to many!!!

PS: You python bozos stay on your lane.

Finally put this puppy in a file and lets convert it to proper beautiful code.

```shell
bin/starlang convert -d beautiful smarter_surprise.sea
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🏁⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🛑⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🔵⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🐒️⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🌟⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫💫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🌟⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫🌟🌟🌟🌟🌟🛑🟠🏁🛑🔵🐒️💫⭐💫🐒️🔵🛑🏁🟠🌟🌟🌟🌟🌟🌟⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🌮️⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🌮️⚫⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫🛑⚫⚫⚫⚫⚫⚫⚫⚫💫⚫💫⚫⚫⚫⚫🛑📝🔵🏁🟠🌮️🌮️💫⭐🌮️🌮️🌮️🌮️🟠🌟🌟🌟
⚫⚫⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫⚫🐒️⚫⚫⚫🐒️⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🐠⚫⚫⚫⚫⚫⚫⚫⚫
⚫🌟🌟🌟🌟⭐🔵🛑🌟🌟⚫⚫🔵⚫⚫⚫⚫⚫🔵⚫⚫⚫⚫⚫⚫⚫⚫⚫🐒️⚫🌮️⚫⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫⚫🐒️⚫⚫⚫⚫⚫🛑⚫⚫⚫⚫⚫⚫⚫🛑⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫🟠⚫⚫⚫⚫⚫⚫
⚫⚫⚫⚫🌟⚫🌟⚫⚫⚫🏁⚫⚫⚫⚫⚫⚫⚫⚫⚫🏁⚫⚫⚫⚫⚫🌟⚫⚫⚫⚫⚫🌟⚫⚫⚫⚫⚫
⚫⚫⚫🌟⚫⚫⚫🌟⚫🟠⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫⚫🟠⚫⚫⚫🌟⚫⚫⚫⚫⚫⚫⚫🌟⚫⚫⚫⚫
```

So, so beautiful I feel like the double rainbow guys. (RIP, you amazing human being).

> [!note] One thing to consider is that this conversion process only converts letters and a couple other characters into beautiful emojis. So if you run out of identifiers, make sure to use a double width character, and emoji or something else, otherwise your code might look ugly. 

You can save the content to a file and run it

```shell
bin/starlang convert -d beautiful smarter_surprise.sea > beatiful_smarter_surprise.sea
bin/starlang run beatiful_smarter_surprise.sea

!
```

> [!note] You might have to press enter after trying to run it, because the interpreter is waiting for input. Here it doesn't matter, so just press enter, or just type your name and watch the interpreter ignore you.

You can also just run the alternative syntax directly, but you suck if you do so. Remember, allways run the beautiful code!

```shell
bin/starlang alt smarter_surprise.sea 

!
```