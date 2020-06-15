#! /usr/bin/perl -w

# Format is strictly "op<space>value<space>coord[<space>coord]*
my(@cages);
my($size) = 0;
while (<STDIN>) {
	chomp;
	my($op, $val, @coords) = split(/ /);

	my(@squares);
	foreach (@coords) {
		s/^\(//;
		s/\)$//;
		my($x, $y) = split(/,/);
		my($square) = {
			'x' => $x,
			'y' => $y
		};
		push(@squares, $square);
		$size = $x if $x > $size;
	}

	my($cage) = {
		'op' => $op,
		'val' => $val,
		'squares' => \@squares
	};

	push(@cages, $cage);
}
$size++; # size is 1-indexed

print("{\n");
print("\t\"size\": $size,\n");
print("\t\"cages\": [\n");
foreach (@cages) {
	print("\t\t{\n");
	print("\t\t\t\"operation\": \"$_->{'op'}\",\n");
	print("\t\t\t\"value\": $_->{'val'},\n");
	print("\t\t\t\"squares\": [\n");
	my(@squares) = @{ $_->{'squares'} };
	foreach (@squares) {
		my(%s) = %{ $_ };
		print("\t\t\t\t{\n");
		print("\t\t\t\t\t\"x\": $s{'x'},\n");
		print("\t\t\t\t\t\"y\": $s{'y'}\n");
		print("\t\t\t\t}");
		if ($_ != $squares[-1]) {
			print(",");
		}
		print("\n");
	}
	print("\t\t\t]\n");
	print("\t\t}");
	if ($_ != $cages[-1]) {
		print(",");
	}
	print("\n");
}
print("\t]\n");
print("}\n");
