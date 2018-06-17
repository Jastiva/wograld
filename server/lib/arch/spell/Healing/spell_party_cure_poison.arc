object spell_party_cure_poison
inherit type_spell
name party cure poison
name_pl party cure poison
skill praying
msg
This prayer cures I<all> poison effect on all party members.
endmsg
other_arch spell_cure_poison
sound wn/heal
face spell_praying.x11
grace 10
level 19
subtype 48
attacktype 1024
path_attuned 256
value 3
invisible 1
no_drop 1
casting_time 20
end

