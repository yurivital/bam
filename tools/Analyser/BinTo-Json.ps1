param(
[String]$inputfile,
[String]$outputfile
)

if( (Test-Path $outputfile) -eq $true){
    Remove-Item -Path $outputfile
}

[System.IO.FileStream]$fs = [System.IO.File]::OpenRead($inputfile);
[System.IO.BinaryReader]$bf =  [System.IO.BinaryReader]::new($fs)


 [Byte[]]$bufferLong = [Byte[]]::CreateInstance([Byte].GetType(),8)
 [Byte[]]$bufferFloat = [Byte[]]::CreateInstance([Byte].GetType(),4)

 $eol = $false
 $cow_type = ""

 # Read binary data from an Big Endian system
 function ToLittleEndian(  ){
      param(
      [System.IO.BinaryReader]$br,
      [int]$nbBytes
    )
    
    $buffer = $br.ReadBytes($nbBytes)
    [Array]::Reverse($buffer)

    if($buffer.Length -eq 8){
        return [System.BitConverter]::ToInt64($buffer,0)  
    }
    if($buffer.Length -eq 4){   
        return [System.BitConverter]::ToSingle($buffer,0)  
    }
    
}

 
# The first line contain the cow type
while( $eol -eq $false)
{
 $buffer = $bf.ReadBytes(2)
 
 [Array]::Reverse($buffer)
 [Byte[]]$b1 = [Byte[]]::CreateInstance([Byte].GetType(),2)
 [Byte[]]$b2 = [Byte[]]::CreateInstance([Byte].GetType(),2)
 $b1[0] = $buffer[0]
 $b2[0] = $buffer[1]
 $val1 = [System.BitConverter]::ToChar( $b1,0)
 $val2 = [System.BitConverter]::ToChar($b2,0)
 if($val1 -eq 13)
 {
    $eol = $true
 }
 else
 {
    $cow_type += [System.Text.UnicodeEncoding]::Unicode.GetString($buffer)
 }
 
}


# Epoch Origin
$origin = New-Object -Type DateTime -ArgumentList 1970, 1, 1, 0, 0, 0, 0

# Read Binary data, ARM processor use BigEndian
# Recording is a size fixed format
# Long (8 bytes) : timestamp in nanosecond
# float (4 bytes) : x value of accelerometer
# float (4 bytes) : y value of accelerometer
# float (4 bytes) : z value of accelerometer

while($bf.BaseStream.Position -lt $bf.BaseStream.Length){

    $timestamp = ToLittleEndian $bf 8
    $x = ToLittleEndian $bf 4
    $y = ToLittleEndian $bf 4
    $z = ToLittleEndian $bf 4

    $date  = $origin.AddMilliseconds($timestamp /1000000)
    $date =  [String]::Format("{0:u}",$date)

    # Create an object for conveniant json formating
    $ob = New-Object -TypeName PSObject
     $ob | Add-Member -MemberType NoteProperty -Name 'cowtype' -Value $cow_type
     $ob | Add-Member -MemberType NoteProperty -Name 'epoch' -Value $timestamp
     $ob | Add-Member -MemberType NoteProperty -Name 'timestamp' -Value $date 
     $ob | Add-Member -MemberType NoteProperty -Name 'x' -Value $x
     $ob | Add-Member -MemberType NoteProperty -Name 'y' -Value $y
     $ob | Add-Member -MemberType NoteProperty -Name 'z' -Value $z

    ConvertTo-Json $ob | Out-File $outputfile -Encoding utf8 -Append
}
