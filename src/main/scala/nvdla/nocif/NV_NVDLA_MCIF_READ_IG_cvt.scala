package nvdla
import chisel3._
import chisel3.experimental._
import chisel3.util._

class NV_NVDLA_MCIF_READ_IG_cvt(implicit conf: cscConfiguration) extends Module {
    val io = IO(new Bundle{
        //general clock
        val nvdla_core_clk = Input(Clock())
        
        //reg2dp
        val reg2dp_rd_ps_cnt = Input(UInt(8.W))

        //mcif2noc
        val mcif2noc_axi_ar_arvalid = Output(Bool())
        val mcif2noc_axi_ar_arready = Input(Bool())
        val mcif2noc_axi_ar_araddr = Output(UInt(conf.NVDLA_MEM_ADDRESS_WIDTH.W))
        val mcif2noc_axi_ar_arid = Output(UInt(8.W))
        val mcif2noc_axi_ar_arlen = Output(UInt(4.W))

        //spt2cvt
        val spt2cvt_req_valid = Input(Bool())
        val spt2cvt_req_ready = Output(Bool())
        val spt2cvt_req_pd = Input(UInt((conf.NVDLA_MEM_ADDRESS_WIDTH+11).W))
        
        //eg2ig
        val eg2ig_axi_vld = Input(Bool())

        //#if (NVDLA_PRIMARY_MEMIF_WIDTH > NVDLA_MEMORY_ATOMIC_WIDTH)
        val cq_wr_pvld = Output(Bool())
        val cq_wr_prdy = Input(Bool())
        val cq_wr_pd = Output(UInt(7.W))
        val cq_wr_thread_id = Output(UInt(3.W))
        
        })
    withClock(io.nvdla_core_clk){
}}

