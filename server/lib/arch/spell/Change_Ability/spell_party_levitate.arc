object spell_party_levitate
inherit type_spell
name party levitate
name_pl party levitate
skill sorcery
msg
Lets everybody levitate above the floor for a while, which makes it impossible
for you to trigger any floor traps or fall into holes.

This spell is needed in some mazes to pass hole traps or other trapped areas.

When you are levitating, you can't fetch items from the ground,
so be careful when you need to grab food for example.
endmsg
other_arch spell_levitate
face spell_sorcery.x11
sp 15
level 19
subtype 48
path_attuned 0
value 3
invisible 1
duration 500
duration_modifier 4
move_type fly_low
no_drop 1
casting_time 20
end

