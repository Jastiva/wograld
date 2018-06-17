object spell_party_armour
inherit type_spell
name party armour
name_pl party armour
skill evocation
msg
Creates fields of force around party
members, reducing the amount of damage the
characters takes from physical attacks. It
does not provide any additional protection to
non-physical attacks, however.
endmsg
other_arch spell_armour
face spell_evocation.x11
sp 12
maxsp 8
ac 2
level 11
subtype 48
resist_physical 20
path_attuned 33
value 3
invisible 1
duration 500
duration_modifier 4
dam_modifier 5
no_drop 1
casting_time 20
end

