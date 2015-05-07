CS 463 Project 3
Wes Chappell
Schuyler Cumbie


For your programming task, what were the challenges that you faced? Where was there competition for resources, and where was there a need for cooperation?

Both languages implement concurrency in different ways. As such, the methods used to solve this problem would be different in both languages. 
In Java, for example, the use of a buffer that stores each follower in a queue as it tries to access put itself inside was useful, but in Haskell this implementation
is much more complicated than necessary and not necessarily native to the language itself.

In your imperative implementation, what aspects of the task were straightforward, and which ones felt laborious?

In Java, the keyword synchronized makes that method a monitor for threads. This is very helpful to make sure that one thread can't override another.
However, shared variables between threads can be a lot less intuitive. If one thread updates a value, how can I make sure all other threads see it before running
code with old data stored in it. notifyAll() can be used to do this. In addition, it is important to note that synchronized only blocks other threads
until the current thread is finished. This could be non-obvious, and could overwrite data that is currently being checked on. To prevent this, an additional
loop should be created at the beginning telling that thread to wait() until the current data is no longer being used. The guarantee of your code being atomic, or
that multiple threads accessing the data in a particular method is thread safe is not always obvious. To write working concurrent code, it is essential to think about
what's happening under the hood, in order to fully understand the possibility of interleaving statements.

What kinds of bugs did you run into for the imperative implementation – deadlock? How did you attempt to inspect what was going wrong with the code?

One way to get deadlock was to not properly update the lock of when a thread was done looking at a value. If not done correctly, then all the threads will keep
waiting for a variable to change that isn't changing. Another way to get deadlock is when a leader thread is trying to fill out the dance card. If it keeps pulling a
follower thread it's already danced with then it'll never stop. There two problems that arise, how to tell if you've already got dances with every follower, and
if you don't, what dances can you do, and are there any followers who are available. The easiest way to inspect problems with the code is to print out every time
a particular thread changes a variable, calls a function, etc, and then check to see if it every finished doing that instruction. If you think there might be a
problem, put a print statment to check it.

In your Haskell implementation, what aspects of the task were straightforward, and which ones felt laborious?

There's a few problems to this implementation. For starters, the most logical way to approach the problem for me was to create a buffer. This is possible in Haskell
but requires heavy boxing and un-boxing of types. Therefore, the solution you suggested was to just create a list with a slot for each follower and leader, its place
in the list represents its id. Some other issue was creating a data type for the Dance Card. You can't just import hashtable, you essentially need to create a list of pairs
one is the dance and the other is the id of the leader/follower you're dancing with. This means you must create your own get and put operations, which took some time
as well. The only really nice thing about Haskell is that you know when your variables/functions are atomic bcause you can literally declare them as such by passing
them into the function atomically.

Again, what kinds of bugs arose during development, and how did you handle them?

The biggest bugs were with trying to get the threads to communicate. In Java, everything is an object which makes communication much easier.
You might try to update the table for thread x so that all other threads can see it, but the end result tables have conflicting values.
But what's worse is trying to debug. Without the use of channels, this is impossible because print statements produce a type, which may not be
the end type of whatever function your error is in. Instead, channels must be created to print out debugs, but creating a channel to do this properly is a challenge
sometimes statements that should print, don't. 

Lastly, what piece of advice do you wish you had received at the start of the assignment?

I think that this assignment was completely and utterly unreasonable. I have spent the majority of the past two weeks of my free time doing this.
Most of my time was spent trying to figure out how to implement the fairly elementary logic of the problem attempting to be solved here only to find that my
solution would not work. Overall, I think that explicit guidelines for functions should have been created, similar to the functional fractals assignments.
In doing this, it prevents people from spending an enormous amounts of time trying something only to find out that it doesn't work. More specifically, I think that
the Haskell portion should have given direct details on how to solve the problem, seeing as most people are just going to go look up how to thread in Haskell only
to discover that what you taught us, TVars are not thread safe, and for the most part, don't really use anything thread-centric - you have to implement it yourself.
Overall, I feel like the point of this assignment was to be quick and simple, only to show how different languages have different ways of developing concurrency.
However, without explicit direction in how to implement this project in a language, there's no way you could have honestly expected anyone to finish this in the time given.

