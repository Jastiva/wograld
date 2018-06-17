object black_metallic_serpent
anim
cobra_black.x11
cobra_black.x12
cobra_black.x13
cobra_black.x12
mina
inherit cobra
name black metallic serpent
face cobra_black.x12
hp 500
maxhp 500
exp 10000
dam 30
attacktype 82945
resist_fire 30
resist_electricity 30
resist_poison 100
randomitems black_metallic_serpent
can_see_in_dark 1
end
more
object black_metallic_serpent_2
anim
cobra_black.x11
cobra_black.x12
cobra_black.x13
cobra_black.x12
mina
inherit cobra_2
face cobra_black.x12
y 1
end

object black_metallic_serpent_lightning
inherit black_metallic_serpent_sleek
name One sleek black metallic serpent
hp 1000
maxhp 1000
exp 65000
speed 1
randomitems black_metallic_serpent_lightning
end

object black_metallic_serpent_lightning_2
inherit black_metallic_serpent_sleek_2
end

object black_metallic_serpent_sleek
inherit black_metallic_serpent
name One sleek black metallic serpent
hp 500
maxhp 500
exp 25000
speed 0.7
end
more
object black_metallic_serpent_sleek_2
inherit black_metallic_serpent_2
end

Object cobra
name giant cobra
face cobra.112
race reptile
anim
cobra.111
cobra.112
cobra.113
cobra.112
mina
color_fg green
speed -0.1
anim_speed 3
monster 1
sleep 1
Wis 10
alive 1
no_pick 1
exp 150
ac -2
wc -10
dam 40
attacktype 1025
hp 30
maxhp 30
level 5
weight 20000
editable 1
end
More
Object cobra_2
name giant cobra
face cobra.212
anim
cobra.211
cobra.212
cobra.213
cobra.212
mina
color_fg green
alive 1
no_pick 1
y 1
end
