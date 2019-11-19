package nvdla

import chisel3._
import chisel3.experimental._
import chisel3.util._
import scala.math._


class project_spec extends nv_large
{
    val NVDLA_FEATURE_DATA_TYPE_INT8 = FEATURE_DATA_TYPE_INT8
    val NVDLA_BPE = 8
    val NVDLA_WEIGHT_DATA_TYPE_INT8 = WEIGHT_DATA_TYPE_INT8
    val NVDLA_WEIGHT_COMPRESSION_ENABLE = WEIGHT_COMPRESSION_ENABLE
    val NVDLA_WINOGRAD_ENABLE = WINOGRAD_ENABLE
    val NVDLA_BATCH_ENABLE = BATCH_ENABLE
    val NVDLA_SECONDARY_MEMIF_ENABLE = SECONDARY_MEMIF_ENABLE
    val NVDLA_SDP_LUT_ENABLE = SDP_LUT_ENABLE
    val NVDLA_SDP_BS_ENABLE = SDP_BS_ENABLE
    val NVDLA_SDP_BN_ENABLE = SDP_BN_ENABLE
    val NVDLA_SDP_EW_ENABLE = SDP_EW_ENABLE
    val NVDLA_BDMA_ENABLE = BDMA_ENABLE
    val NVDLA_RUBIK_ENABLE = RUBIK_ENABLE
    val NVDLA_RUBIK_CONTRACT_ENABLE = RUBIK_CONTRACT_ENABLE
    val NVDLA_RUBIK_RESHAPE_ENABLE = RUBIK_RESHAPE_ENABLE
    val NVDLA_PDP_ENABLE = PDP_ENABLE
    val NVDLA_CDP_ENABLE = CDP_ENABLE
    val NVDLA_RETIMING_ENABLE = RETIMING_ENABLE
    val NVDLA_MAC_ATOMIC_C_SIZE = MAC_ATOMIC_C_SIZE
    val NVDLA_MAC_ATOMIC_K_SIZE = MAC_ATOMIC_K_SIZE
    val NVDLA_MEMORY_ATOMIC_SIZE = MEMORY_ATOMIC_SIZE
    val NVDLA_MAX_BATCH_SIZE = MAX_BATCH_SIZE
    val NVDLA_CBUF_BANK_NUMBER = CBUF_BANK_NUMBER
    val NVDLA_CBUF_BANK_WIDTH = CBUF_BANK_WIDTH
    val NVDLA_CBUF_BANK_DEPTH = CBUF_BANK_DEPTH
    val NVDLA_SDP_BS_THROUGHPUT = if(SDP_BS_ENABLE) SDP_BS_THROUGHPUT else 0
    val NVDLA_SDP_BN_THROUGHPUT = if(SDP_BN_ENABLE) SDP_BN_THROUGHPUT else 0
    val NVDLA_SDP_EW_THROUGHPUT = if(SDP_EW_ENABLE) SDP_EW_THROUGHPUT else 0 
    val NVDLA_SDP_EW_THROUGHPUT_LOG2 = if(SDP_EW_ENABLE) log2Ceil(NVDLA_SDP_EW_THROUGHPUT) else 0
    val NVDLA_SDP_MAX_THROUGHPUT = Array(NVDLA_SDP_EW_THROUGHPUT, NVDLA_SDP_BN_THROUGHPUT,NVDLA_SDP_BS_THROUGHPUT).reduceLeft(_ max _)
    val NVDLA_SDP2PDP_WIDTH = NVDLA_SDP_MAX_THROUGHPUT * NVDLA_BPE
    val NVDLA_PDP_THROUGHPUT = PDP_THROUGHPUT
    val NVDLA_CDP_THROUGHPUT = CDP_THROUGHPUT
    val NVDLA_PRIMARY_MEMIF_LATENCY = PRIMARY_MEMIF_LATENCY
    val NVDLA_SECONDARY_MEMIF_LATENCY = SECONDARY_MEMIF_LATENCY
    val NVDLA_PRIMARY_MEMIF_MAX_BURST_LENGTH = PRIMARY_MEMIF_MAX_BURST_LENGTH
    val NVDLA_PRIMARY_MEMIF_WIDTH = PRIMARY_MEMIF_WIDTH
    val NVDLA_SECONDARY_MEMIF_MAX_BURST_LENGTH = SECONDARY_MEMIF_MAX_BURST_LENGTH
    val NVDLA_SECONDARY_MEMIF_WIDTH = SECONDARY_MEMIF_WIDTH
    val NVDLA_MEM_ADDRESS_WIDTH = MEM_ADDRESS_WIDTH
    val NVDLA_MEMIF_WIDTH = if(SECONDARY_MEMIF_ENABLE)
                            Array(NVDLA_PRIMARY_MEMIF_WIDTH, NVDLA_SECONDARY_MEMIF_WIDTH, NVDLA_MEMORY_ATOMIC_SIZE*NVDLA_BPE).reduceLeft(_ max _)
                            else
                            max(NVDLA_PRIMARY_MEMIF_WIDTH, NVDLA_MEMORY_ATOMIC_SIZE*NVDLA_BPE)
    val NVDLA_DMA_RD_SIZE = 15
    val NVDLA_DMA_WR_SIZE = 13
    val NVDLA_DMA_MASK_BIT = NVDLA_MEMIF_WIDTH / NVDLA_BPE / NVDLA_MEMORY_ATOMIC_SIZE
    val NVDLA_DMA_RD_RSP = NVDLA_MEMIF_WIDTH + NVDLA_DMA_MASK_BIT
    val NVDLA_DMA_WR_REQ = NVDLA_MEMIF_WIDTH + NVDLA_DMA_MASK_BIT + 1
    val NVDLA_DMA_WR_CMD = NVDLA_MEM_ADDRESS_WIDTH + NVDLA_DMA_WR_SIZE +1
    val NVDLA_DMA_RD_REQ = NVDLA_MEM_ADDRESS_WIDTH + NVDLA_DMA_RD_SIZE
    val NVDLA_MEMORY_ATOMIC_LOG2 = log2Ceil(NVDLA_MEMORY_ATOMIC_SIZE)
    val NVDLA_PRIMARY_MEMIF_WIDTH_LOG2 = log2Ceil(NVDLA_PRIMARY_MEMIF_WIDTH/8)
    val NVDLA_SECONDARY_MEMIF_WIDTH_LOG2 = if(SECONDARY_MEMIF_ENABLE)
                                           log2Ceil(NVDLA_SECONDARY_MEMIF_WIDTH/8)
                                           else
                                           None
    val NVDLA_MEMORY_ATOMIC_WIDTH =  NVDLA_MEMORY_ATOMIC_SIZE*NVDLA_BPE
    val NVDLA_MCIF_BURST_SIZE = NVDLA_PRIMARY_MEMIF_MAX_BURST_LENGTH*NVDLA_DMA_MASK_BIT
    val NVDLA_MCIF_BURST_SIZE_LOG2 = log2Ceil(NVDLA_MCIF_BURST_SIZE)
    val NVDLA_NUM_DMA_READ_CLIENTS = NUM_DMA_READ_CLIENTS
    val NVDLA_NUM_DMA_WRITE_CLIENTS = NUM_DMA_WRITE_CLIENTS
    val DESIGNWARE_NOEXIST = true
    val PDP_SINGLE_LBUF_WIDTH = 16*NVDLA_MEMORY_ATOMIC_SIZE/NVDLA_PDP_THROUGHPUT
    val PDP_SINGLE_LBUF_DEPTH = NVDLA_PDP_THROUGHPUT*(NVDLA_BPE+6)

    val NVDLA_VMOD_PRIMARY_BANDWIDTH = NVDLA_PRIMARY_MEMIF_WIDTH/NVDLA_BPE/4
    val NVDLA_VMOD_SDP_MRDMA_OUTPUT_THROUGHPUT = NVDLA_SDP_MAX_THROUGHPUT
    val NVDLA_VMOD_SDP_BRDMA_OUTPUT_THROUGHPUT = 4*NVDLA_SDP_BS_THROUGHPUT
    val NVDLA_VMOD_SDP_NRDMA_OUTPUT_THROUGHPUT = 4*NVDLA_SDP_BN_THROUGHPUT
    val NVDLA_VMOD_SDP_ERDMA_OUTPUT_THROUGHPUT = 4*NVDLA_SDP_EW_THROUGHPUT
    val NVDLA_VMOD_CDP_RDMA_OUTPUT_THROUGHPUT_USE = min(NVDLA_CDP_THROUGHPUT, NVDLA_VMOD_PRIMARY_BANDWIDTH)
    val NVDLA_VMOD_PDP_RDMA_OUTPUT_THROUGHPUT_USE = min(NVDLA_PDP_THROUGHPUT, NVDLA_VMOD_PRIMARY_BANDWIDTH)
    val NVDLA_VMOD_SDP_MRDMA_OUTPUT_THROUGHPUT_USE = min(NVDLA_VMOD_SDP_MRDMA_OUTPUT_THROUGHPUT, NVDLA_VMOD_PRIMARY_BANDWIDTH)
    val NVDLA_VMOD_SDP_BRDMA_OUTPUT_THROUGHPUT_USE = min(NVDLA_VMOD_SDP_BRDMA_OUTPUT_THROUGHPUT, NVDLA_VMOD_PRIMARY_BANDWIDTH)
    val NVDLA_VMOD_SDP_NRDMA_OUTPUT_THROUGHPUT_USE = min(NVDLA_VMOD_SDP_NRDMA_OUTPUT_THROUGHPUT, NVDLA_VMOD_PRIMARY_BANDWIDTH)
    val NVDLA_VMOD_SDP_ERDMA_OUTPUT_THROUGHPUT_USE = min(NVDLA_VMOD_SDP_ERDMA_OUTPUT_THROUGHPUT, NVDLA_VMOD_PRIMARY_BANDWIDTH)
    val NVDLA_VMOD_CDP_RDMA_LATENCY_FIFO_DEPTH  = max(4,ceil(NVDLA_PRIMARY_MEMIF_LATENCY*NVDLA_VMOD_CDP_RDMA_OUTPUT_THROUGHPUT_USE/(NVDLA_MEMIF_WIDTH/NVDLA_BPE))).toInt
    val NVDLA_VMOD_PDP_RDMA_LATENCY_FIFO_DEPTH  = max(4,ceil(NVDLA_PRIMARY_MEMIF_LATENCY*NVDLA_VMOD_PDP_RDMA_OUTPUT_THROUGHPUT_USE/(NVDLA_MEMIF_WIDTH/NVDLA_BPE))).toInt
    val NVDLA_VMOD_SDP_MRDMA_LATENCY_FIFO_DEPTH = max(4,ceil(NVDLA_PRIMARY_MEMIF_LATENCY*NVDLA_VMOD_SDP_MRDMA_OUTPUT_THROUGHPUT_USE/(NVDLA_MEMIF_WIDTH/NVDLA_BPE))).toInt
    val NVDLA_VMOD_SDP_BRDMA_LATENCY_FIFO_DEPTH = max(4,ceil(NVDLA_PRIMARY_MEMIF_LATENCY*NVDLA_VMOD_SDP_BRDMA_OUTPUT_THROUGHPUT_USE/(NVDLA_MEMIF_WIDTH/NVDLA_BPE))).toInt
    val NVDLA_VMOD_SDP_NRDMA_LATENCY_FIFO_DEPTH = max(4,ceil(NVDLA_PRIMARY_MEMIF_LATENCY*NVDLA_VMOD_SDP_NRDMA_OUTPUT_THROUGHPUT_USE/(NVDLA_MEMIF_WIDTH/NVDLA_BPE))).toInt
    val NVDLA_VMOD_SDP_ERDMA_LATENCY_FIFO_DEPTH = max(4,ceil(NVDLA_PRIMARY_MEMIF_LATENCY*NVDLA_VMOD_SDP_ERDMA_OUTPUT_THROUGHPUT_USE/(NVDLA_MEMIF_WIDTH/NVDLA_BPE))).toInt
    val NVDLA_VMOD_DMA_LAT_FIFO_DEPTH_MAX = 512

    val NVDLA_MAC_ATOMIC_C_SIZE_LOG2 = log2Ceil(NVDLA_MAC_ATOMIC_C_SIZE)
    val NVDLA_MAC_ATOMIC_K_SIZE_LOG2 = log2Ceil(NVDLA_MAC_ATOMIC_K_SIZE)
    val NVDLA_MAC_ATOMIC_K_SIZE_DIV2 = NVDLA_MAC_ATOMIC_K_SIZE/2
    val NVDLA_CBUF_BANK_NUMBER_LOG2 = log2Ceil(NVDLA_CBUF_BANK_NUMBER)
    val NVDLA_CBUF_BANK_WIDTH_LOG2 = log2Ceil(NVDLA_CBUF_BANK_WIDTH)
    val NVDLA_CBUF_BANK_DEPTH_LOG2 = log2Ceil(NVDLA_CBUF_BANK_DEPTH)
    val NVDLA_CBUF_DEPTH_LOG2 = log2Ceil(NVDLA_CBUF_BANK_NUMBER)+log2Ceil(NVDLA_CBUF_BANK_DEPTH)
    val NVDLA_CBUF_ENTRY_WIDTH = NVDLA_MAC_ATOMIC_C_SIZE*NVDLA_BPE
    val NVDLA_CBUF_WIDTH_LOG2 = log2Ceil(NVDLA_CBUF_ENTRY_WIDTH)
    val NVDLA_CBUF_WIDTH_MUL2_LOG2 = log2Ceil(NVDLA_CBUF_ENTRY_WIDTH)+1
    val NVDLA_BPE_LOG2 = log2Ceil(NVDLA_BPE)
    val NVDLA_MAC_RESULT_WIDTH = NVDLA_BPE*2+NVDLA_MAC_ATOMIC_C_SIZE_LOG2
    val NVDLA_CC_ATOMC_DIV_ATOMK = NVDLA_MAC_ATOMIC_C_SIZE/NVDLA_MAC_ATOMIC_K_SIZE
    val NVDLA_CACC_SDP_WIDTH = ((32*NVDLA_SDP_MAX_THROUGHPUT) +2)
    val NVDLA_CACC_SDP_SINGLE_THROUGHPUT = 32

    val NVDLA_CDMA_GRAIN_MAX_BIT = log2Ceil(NVDLA_CBUF_BANK_DEPTH*NVDLA_CBUF_BANK_WIDTH*(NVDLA_CBUF_BANK_NUMBER-1)/(NVDLA_MEMORY_ATOMIC_SIZE))

    val useFPGA = true
    
    var XSDB_SLV_DIS = true
    var FPGA = true
    var SYNTHESIS = true
    var VLIB_BYPASS_POWER_CG = true
    var NV_FPGA_SYSTEM = true
    var NV_FPGA_FIFOGEN = true
    var NV_FPGA_UNIT = true

    if(!useFPGA){
        XSDB_SLV_DIS = false
        FPGA = false
        SYNTHESIS = false
        VLIB_BYPASS_POWER_CG = false
        NV_FPGA_SYSTEM = false
        NV_FPGA_FIFOGEN = false
        NV_FPGA_UNIT = false
    }
}
 

