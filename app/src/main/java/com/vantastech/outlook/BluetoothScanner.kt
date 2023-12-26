package com.vantastech.outlook

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.ParcelUuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BluetoothScanner(private val context: Context) {
    private val bluetoothAdapter: BluetoothAdapter by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }
    private val bluetoothLeScanner: BluetoothLeScanner?
        get() = bluetoothAdapter.bluetoothLeScanner
    private val scope = CoroutineScope(Dispatchers.Default)
    private val interestManager = InterestManager(context)
    private val matcher = Matcher(context)

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            scope.launch {
                matcher.match(result)
            }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            super.onBatchScanResults(results)
            results.forEach { result ->
                scope.launch {
                    matcher.match(result)
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            // Handle scan failure, log or retry scanning as needed
        }
    }

    fun startScanning() {
        bluetoothLeScanner?.startScan(scanCallback)
    }

    fun stopScanning() {
        bluetoothLeScanner?.stopScan(scanCallback)
    }
}
