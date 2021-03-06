package mouduleTester.exampleModule

import Chisel.switch
import chisel3.stage.ChiselStage
import chisel3.util.{Enum, is}
import chisel3.{Bool, Bundle, Input, Module, Output, RegInit, UInt, when}

class EnumStateMachine extends Module {

    val io = IO(new Bundle {
      val noise = Input(Bool())
      val trash = Input(Bool())
      val food  = Input(Bool())
      val action = Output(UInt())
    })
    val hide :: wander :: rummage :: eat :: Nil = Enum(4)
    val state = RegInit(hide)
    when (state === hide) {
      when (!io.noise) { state := wander }
    }
    .elsewhen (state === wander) {
      when (io.noise) { state := hide }.elsewhen (io.trash) { state := rummage }
    }
    .elsewhen (state === rummage) {
      when (io.noise) { state := hide }.elsewhen (io.food) { state := eat }
    }
    .elsewhen (state === eat) {
      when (io.noise) { state := hide }.elsewhen (!io.food) { state := wander }
    }
    io.action := state
}

class EnumStateMachine2 extends Module { // using switch
  val io = IO(new Bundle {
    val noise = Input(Bool())
    val trash = Input(Bool())
    val food  = Input(Bool())
    val action = Output(UInt())
  })
  val hide :: wander :: rummage :: eat :: Nil = Enum(4)
  val state: UInt = RegInit(hide)

  switch(state) {
    is (hide) {
      when (!io.noise) { state := wander }
    }
    is (wander) {
      when (io.noise) { state := hide }
      .elsewhen (io.trash) { state := rummage }
    }
    is (rummage) {
      when (io.noise) { state := hide }
      .elsewhen (io.food) { state := eat }
    }
    is (eat) {
      when (io.noise) { state := hide }
      .elsewhen (!io.food) { state := wander }
    }
  }

  io.action := state

}


//object MyCounter extends App {
//  (new ChiselStage).emitVerilog(new EnumStateMachine())
////  (new ChiselStage).emitVerilog(new EnumStateMachine2())
//
//}
//
