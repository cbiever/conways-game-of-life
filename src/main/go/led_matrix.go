package main

import (
  "bufio"
	"net"
  "net/textproto"
	"log"
	"os"
	"bitbucket.org/gmcbay/i2c/HT16K33"
)

func main() {
	devices, err := HT16K33.ParseDevices("0x70:1")

	if err != nil {
		log.Print("Error connecting to 8x8 LED matrix: %s\n", err.Error());
		os.Exit(1)
	}

	for _, device := range devices {
		device.ImmediateUpdate = false
	}

	ln, err := net.Listen("tcp", ":8081")
	if err != nil {
		log.Print("Error listening:", err.Error())
		os.Exit(1)
	}

	for {
		conn, err := ln.Accept()

		if err != nil {
			log.Print("Error accepting:", err.Error())
			os.Exit(1)
		}

    reader := bufio.NewReader(conn)
    textproto := textproto.NewReader(reader)
    var command string
    for err == nil { 
	  	command, err = textproto.ReadLine()
	  	if err == nil {
	  		for i := 0; i < len(command) && i < 64; i++ {
	  			devices[0].SetPixel(byte(i / 8), byte(i % 8), command[i] == '*');
	  		}
	  		devices[0].WriteDisplay()
  		} else {
  			log.Print("Error reading:", err.Error())
  			conn.Close()
  		}
    }
	}
}
