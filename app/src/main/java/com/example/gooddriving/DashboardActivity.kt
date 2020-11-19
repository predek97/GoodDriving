package com.example.gooddriving


import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.github.pires.obd.commands.engine.RPMCommand
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.reflect.Method


class DashboardActivity : BasicLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.selectedItemId = R.id.navigation_dashboard
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val bluetoothButton : Button = findViewById(R.id.bluetooth_button)
        bluetoothButton.setOnClickListener(onBluetoothButtonSelectedListener)
    }

    protected val onBluetoothButtonSelectedListener = View.OnClickListener {

        val bAdapter = BluetoothAdapter.getDefaultAdapter()
        val intVal : Int = 10
        if (!bAdapter.isEnabled) {
            val eintent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(eintent, intVal)
        }

        // Get paired devices.
        val list : ArrayList<BluetoothDevice> = ArrayList()
        for( device: BluetoothDevice in bAdapter.bondedDevices) {
            list.add(device)
        }
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.choose_bluetooth_device_dialog)
        val adapter = BluetoothDeviceAdapter(this, android.R.layout.simple_list_item_1, list)
        var listView = dialog.findViewById<ListView>(R.id.select_device_list)
        listView.adapter = adapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            handleDevice(device)
        }
        dialog.show()
    }

    protected fun handleDevice(device: BluetoothDevice) {
        val m: Method = device.javaClass.getMethod(
            "createInsecureRfcommSocket", *arrayOf<Class<*>?>(
                Int::class.javaPrimitiveType
            )
        )
        var bluetoothSocket = m.invoke(device, 1) as BluetoothSocket
        bluetoothSocket.connect()
        val socket = device.createInsecureRfcommSocketToServiceRecord(device.uuids[0].uuid)
        socket.use { socket ->
            socket.connect()
            val command = RPMCommand()
            command.run(socket.inputStream, socket.outputStream)
            Toast.makeText(this, command.result, command.result.length).show()
        }

        socket.close()
    }

    protected class BluetoothDeviceAdapter(context: Context, layout: Int, devices:MutableList<BluetoothDevice>) : ArrayAdapter<BluetoothDevice>(context, layout, devices) {
        override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
            var listItem = convertView
            if (listItem == null) listItem =
                LayoutInflater.from(context).inflate(R.layout.device_layout, parent, false)
            val currentDevice = getItem(pos)

            val name = listItem!!.findViewById<View>(R.id.textView_name) as TextView
            name.text = currentDevice!!.name

            val address = listItem!!.findViewById<View>(R.id.textView_address) as TextView
            address.text = currentDevice!!.address

            return listItem!!
        }
    }

}
