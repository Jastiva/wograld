Object book_build_instructions
name    Building Instructions
name_pl Building Instructions
nrof 1
face book_read.111
type 8
subtype 3
skill literacy
material 1
value 13
weight 3000
editable 128
value 20000
msg
B<Basic Building Instructions>

To build your own home, you can use different items:

* generic builder: this tool lets you build anything. You need one to build.

* generic destroyer: this tool lets you destroy a built item. This will remove
any built item, except floors and walls.

* materials: you need those to build items. For instance, a 'Wood floor
material' enables you to build a woodfloor.

To build an item:

* apply the builder

* mark the material of the item you want to build

* fire the builder in the desired direction.

Of course, the material is used when you build, so you need to buy quite a few.


B<Building Floors and Walls>

Building enables you to put walls and floors where you want.
Building a floor lets you remove walls. This is the only way to remove them.
You can change an existing floor by building another on top. Same goes for walls.
All created floors are magic, and will preserve items you put on them.


B<Building Restrictions>

To build on a spot, all items on this spot must be 'buildable'. Buildable
items will display 'this is a buildable item' when examined.

I<There are several exceptions though:>

* In all cases you can build on marking runes as they are used
for making gates and buttons work together.

* Also you can build signs and talking books on of books and or scrolls,
because they are needed for specifying the message.

* Also, buttons, levers and pedestals can be build
underneath gates. Don't worry, you should have some clear messages on why you
can't build.


B<Building Gates and Buttons/Pedestals>

Some items, like gates, require buttons, pedestals, ..., something to be opened
and closed. To build a gate with its associated button:

* put a marking rune at the spot where you wish to build the door.
The text can be whatever you want, make sure to remember it.

* build the gate on this spot.

* put a marking rune at the spot where you with to put the button.
Use the same text as for the door.

* build the button.

Your button will now activate the gate when you apply it.

Note that you can link together as many gates, buttons, pedestals, as you want.
As long as you use the same text on the marking rune, they will all work together.

To build gates activated by other buttons, just use another text.
Normally all gates with a single marking rune text are either all up or all
down at a given time, however if you want one open while another is closed,
make use of inverted gates.

Also, it is possible to make buttons, levers, and
pedestals underneath gates, however it requires things to be made in a specific
order:

* make 2 marking runes on the square that you want to build the button and gate on.

* build the gate first.

* then build the button underneath it.

This is useful for making a gate avoid crushing people underneath it. Also, you
can use this same technique with inverted gates in order to make a square that
pushes objects placed on it off of it.


B<Removing Items>

To remove built items, you need a 'generic destroyer'.  Simply apply it, and
fire in the desired direction. It will remove the first built item from the
location.  Warning: a destroyer will remove items starting from the lowest
order on the spot. Basically, this means the first removed item will be a
pedestal (underground), then items on floor.  Note that the destroyer will not
remove walls or floors.


B<Signs and Talking Books>

When making signs and talking (or listening) books, you need to place a book or
scroll with the text you want on the square you're building on just like a
marking rune.

With the talking/listening books, they will take the name and
picture of the book used to create them as well.

For the talking and listening
books, you also need to connect them with a marking rune just like gates
buttons and levers, which in the case of the talking books, makes the book say
it's message when activated, and in the case of listening books, it causes
saying the phrase that the book looks for to act like pulling a lever.

For the listening books, the message must be in this format:

<keyword><newline>
<message>

For instance:

I<sesame open>
I<Click.>

Which will make it activate upon the somebody within two squares saying "sesame
open", and will additionally say "Click." upon activation.


B<Building Other Items>

All items not requiring any special handling are built simply by marking the
appropriate material and firing the builder.  Note that, to avoid any issue,
those items can not be picked up.  Note also that you can build an item on
another.
endmsg
client_type 1041
end
