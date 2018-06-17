object spell_party_protection_from_magic
inherit type_spell
name party protection from magic
name_pl party protection from magic
skill praying
msg
Reduces damage from
magical attacks or spells. There are a few
spells that do not hit with a magical attack;
this spell does nothing to reduce those
effects.
endmsg
other_arch spell_protection_from_magic
face spell_praying.x11
grace 45
maxgrace 40
level 25
subtype 48
resist_magic 30
path_attuned 1
value 3
invisible 1
duration 500
duration_modifier 4
dam_modifier 3
no_drop 1
casting_time 24
end

