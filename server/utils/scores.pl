#!/usr/bin/perl
# scores.pl
# (c) Pertti Karppinen a.k.a roWer <pjka@iki.fi>
#
# thanks for Sami Yl�nen a.k.a klux for some cleaning up
#
# This  program  is  free  software; you can redistribute it
# and/or modify it under the terms of the GNU General Public
# License  as  published  by  the  Free Software Foundation;
# either version 2 of the License, or (at your  option)  any
# later version.
# This  program  is distributed in the hope that it will be
# useful, but WITHOUT ANY WARRANTY; without even the implied
# warranty  of  MERCHANTABILITY  or FITNESS FOR A PARTICULAR
# PURPOSE.  See the GNU  General  Public  License  for  more
# details.

# This should get run out of cron like:
# 0,5,10,15,20,25,30,35,40,45,50,55 * * * * $HOME/wograld/var/www/bin/scores.pl > /dev/null 2>&1
# As a note, if you have a lot of players, you may want to run this less often.  This
# reads through all the player files on the server to generate this information.
#

use File::Basename;


#the name of the server admin
my $admin_name="Some Admin";

#the email of the server admin
my $admin_email="Some.Admin@Some.Where";

#html output file
my $outfile="/var/www/html/cscores.html";

#complete url to the output
my $scoreurl="/cscores.html";

my $timezone=`date +%Z`;

#Your Wograld folder
my $wograld_home="/usr/games/wograld";

#background color of the webpage
my $bgcolor="#eeeeee";

#background color of the outer table
my $outertablebgcolor="#dddddd";

#background color of the inner table
my $innertablebgcolor="#dddddd";

#background color for <tr>
my $tabletrcolor="#a9b4f2";

#the title of your score webpage
my $title="Wograld scores";


#the levels =)
# MSW Note - we should really be clever and read the exp_table
# file to dynamically generate this.

my @levels=(0,2000,4000, 8000,
	    16000,32000,64000,125000,250000,
	    500000,900000,1400000,2000000,2600000,
	    3300000,4100000,4900000,5700000,6600000,
	    7500000,8400000,9300000,10300000,11300000,
	    12300000,13300000,14400000,15500000,16600000,
	    17700000,18800000,19900000,21100000,22300000,
	    23500000,24700000,25900000,27100000,28300000,
	    29500000,30800000,32100000,33400000,34700000,
	    36000000,37300000,38600000,39900000,41200000,
	    42600000,44000000,45400000,46800000,48200000,
	    49600000,51000000,52400000,53800000,55200000,
	    56600000,58000000,59400000,60800000,62200000,
	    63700000,65200000,66700000,68200000,69700000,
	    71200000,72700000,74200000,75700000,77200000,
	    78700000,80200000,81700000,83200000,84700000,
	    86200000,87700000,89300000,90900000,92500000,
	    94100000,95700000,97300000,98900000,100500000,
	    102100000,103700000,105300000,106900000,108500000,
	    110100000,111700000,113300000,114900000,116500000,
	    118100000,119700000,121300000,122900000,124500000,
	    126100000,127700000,129300000,130900000,785400000,
	    1570800000);


my $DEBUG=0;

#prototypes
sub parse_file($);
sub html_print_player($@);
sub html_print_table_header();
sub html_print_header();
sub html_print_footer();
sub read_dms();

my @files= glob("$wograld_home/var/wograld/players/*/*.pl");
foreach(sort @files) {
    print "$_\n" if $DEBUG;
    parse_file($_);
}
for($i=0;$i<$#scores+1;$i++)  {
    my $exp=$scores[$i][6];
    my $name=$scores[$i][0];
    $score_hash{$name}=$exp;
    $ranking{$name}=$i;
}
my %dms;
read_dms();

my $rank=1;
$saved_exp=-1;
$saved_rank=-1;
open(OUT,">$outfile") or die("Couldn't open outputfile $outfile: $!\n");
my $ofh=select(OUT);
$|=1;
html_print_header();
html_print_table_header();
foreach(sort {$score_hash{$b} <=> $score_hash{$a}} keys %ranking) {
  my $i=$ranking{$_};
  my @tmp;
  print "$scores[$i][0]\t$scores[$i][6]\n" if $DEBUG;
  for($j=0;$j<12;$j++)  {
    $tmp[$j]=$scores[$i][$j];
  }
  html_print_player($rank++,@tmp);
}
print "</table>\n";
html_print_footer();
close(OUT);
select($ofh);
exit 0;

sub parse_file($) {
  my $player_file=shift;
  my($name,$title,$race,$class,$killer,$exp,$map,$maxhp,$maxsp,$maxgrace,$level,$god);
  my $state=0;
  my $count=0;
  my $no_class=0;
  $killer="left";
  $god="&nbsp;";
  open(PLAYER_FILE,"$player_file") or die("autch $!");
  while(<PLAYER_FILE>) {
      if(/no_class_face_change/) {
	  $no_class=1;
       }
      if($state==0) {
	  if(/^title/) {
	      /^title\s*(.*)$/;
	      $title=$1;
	      $count++;
	      next;
	  } elsif(/^map/) {
	      chomp;
	      $map=basename($_);
	      $count++;
	      next;
	  }
	  $state=2 if($count==2);
	  $state=2 if(/^arch.*_player/ || /^arch.*pl_.*/);
	  next unless($state==2);
      }
      if($state==2||$state==3) {
	  next unless($state==3||/^arch.*_player/ || /^arch.*pl_.*/);
	  next if(/^name_pl/);
	  if($state==2&&/^arch.*_player/) {
	      /^arch\s*(.*?)_player/;
	      $race=$1;
	      $count++;
	      $state=3;
	      next;
	  }
	  if($state==2&&/^arch.*pl_.*/) {
	      /^arch.*pl_(\S*)/;
	      $race=$1;
	      $race =~ s/_/ /g;
	      $count++;
	      $state=3;
	      next;
	  }
	  if(/^title/) {
	      /^title\s*(.*)$/;
	      $title=$1;
	      $count++;
	      next;
	  }
	  if(/^name/) {
	      /^name\s*(.*)$/;
	      $name=$1;
	      $count++;
	      next;
	  }
	  if(/^face/) {
	      /^face\s*([^_]*).*\.\d+/;
	      $class=$1;
	      $count++;
	      next;
	  }
	  if(/^maxhp/) {
	      /^maxhp\s*(\d*)/;
	      $maxhp=$1;
	      $count++;
	      next;
	  }
	  if(/^maxsp/) {
	      /^maxsp\s*(\d*)/;
	      $maxsp=$1;
	      $count++;
	      next;
	  }
	  if(/^maxgrace/) {
	      /^maxgrace\s*(\d*)/;
	      $maxgrace=$1;
	      $count++;
	      next;
	  }
	  if(/^exp/) {
	      /^exp\s*(\d*)/;
	      $exp=$1;
	      $count++;
	      next;
	  }
	  if(/^level/) {
	      /^level\s*(\d*)/;
	      $level=$1;
	      $count++;
	      next;
	  }
	  #	  $state=4 if($count==8);
	  $state=4 if(/^arch/ || /^end/);
	  next;
      } elsif($state==4) {
	  next unless(/^arch skill_praying/);
	  $state=5;
	  next;
      } elsif($state==5) {
	  $state=6 if(/^end/);
	  next unless(/^title/);
	  /^title\s*(.*)/;
	  $god=$1;
	  $state=6;
      }
  }
  close(PLAYER_FILE);
  $class=$race if($no_class);
  $title=$class unless defined $title;
  if(!defined $level) {
      for($level=0;$level<$#levels;$level++)  {
	  last if($exp<$levels[$level]);
      }
  }
  if(defined $exp) {
      my @tmp= ($name, $title ,$race,$class,$killer,$map,$exp,$level,$maxhp,$maxsp,$maxgrace,$god);
      push(@scores,\@tmp);
      print STDERR "$name the $title ($race $class) $killer the game on map $map with $exp points (level $level)" if $DEBUG;
      print STDERR " and maxhp of $maxhp, maxsp of $maxsp and maxgrace" if $DEBUG;
      print STDERR " (from $god)" if (defined $god && $DEBUG);
      print STDERR " of $maxgrace.\n" if $DEBUG;
  }

}

sub html_print_player($@) {
  my($rank,@table)=@_;
  my $name = $table[0];
  my $title = $table[1];
  my $race = $table[2];
  my $class = $table[3];
  my $killer = $table[4];
  my $map = $table[5];
  my $exp = $table[6];
  my $level = $table[7];
  my $maxhp = $table[8];
  my $maxsp = $table[9];
  my $maxgrace = $table[10];
  my $god = $table[11];
  print "<tr bgcolor=\"$tabletrcolor\">\n";
  if ($saved_exp!=$exp) {
    $saved_exp=$exp;
    $saved_rank=$rank;
  }
  if ($saved_rank<11) {
    print " <td align=right><b>$saved_rank.</b></td>\n";
  } else {
    print " <td align=right>$saved_rank.</td>\n";
  }
  if ($dms{$name}) {
    print " <td>$name the $title <font color=red size=-3>DM</font></td>\n";
  } else {
    print " <td>$name the $title</td>\n";
  }
  print " <td>$race</td>\n";
  print " <td align=right>$exp</td>\n" unless($exp>=785400000);
  print " <td align=right><font color=red>$exp</font></td>\n" if($exp>=785400000);
  print " <td align=right>$level</td>\n";
  print " <td align=right>$maxhp</td>\n";
  print " <td align=right>$maxsp</td>\n";
  print " <td align=right>$maxgrace</td>\n";
  print " <td>$god</td>\n";
  print " <td>$map</td>\n";
  print "</tr>\n";
}

sub html_print_table_header() {
  print "<table border=0 cellpadding=2 cellspacing=1 class=\"inner_table\">\n";
  print "<tr bgcolor=\"$tabletrcolor\">\n";
  print " <td align=\"center\">Rank</td>\n";
  print " <td align=\"center\">Character</td>\n";
  print " <td align=\"center\">Race</td>\n";
  print " <td align=\"center\">Score</td>\n";
  print " <td align=\"center\">Level</td>\n";
  print " <td align=\"center\">MaxHP</td>\n";
  print " <td align=\"center\">MaxSP</td>\n";
  print " <td align=\"center\">MaxGP</td>\n";
  print " <td align=\"center\">God</td>\n";
  print " <td align=\"center\">Location</td>\n";
  print "</tr>\n";
}
sub html_print_header() {
  print "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n";
  print "<html><head><title>$title</title>\n";
  print "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n";
  print "<meta http-equiv=\"refresh\" content=\"300; url=$scoreurl\">\n";
  print << "EOF";

	<STYLE TYPE="text/css">
	<!--
	.outer_table {
	background-color: $outertablebgcolor;
	border: 1px #4350b0 solid;
	}
	.inner_table {
	background-color: $innertablebgcolor;
        border: 2px #4350b0 solid;
	}
	-->
	</STYLE>

EOF

  print "</head>\n";
  print "<body bgcolor=\"$bgcolor\"><center>\n";
  print "<table border=0 class=\"outer_table\" align=\"center\"><tr><td align=\"center\">\n";
  print "<h1>$title</h1>\n";
}
sub html_print_footer()
{
  my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) =
    localtime(time);
  $year+=1900;
  my @months=("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
  my @days=("Sun","Mon","Tue","Wed","Thu","Fri","Sat");
  print "<a href=\"/\">Back</a>\n</center>\n";
  print "<hr>\n<address><a href=\"mailto:$admin_email\">$admin_name</a></address>\n";
  printf ("Last modified: $days[$wday] $months[$mon] $mday %2.2d:%2.2d:%2.2d $timezone $year\n",$hour,$min,$sec);
  print "</td></tr></table>\n";
  print "</body></html>\n";
}

sub read_dms()
{
  open(DMLIST, "$wograld_home/etc/wograld/dm_file");
  while(<DMLIST>) {
    next if(/^\s*#/);
    chomp;
    /\s*([^\s:]+):/;
    $dms{$1}=1;
  }
  close(DMLIST);
}
