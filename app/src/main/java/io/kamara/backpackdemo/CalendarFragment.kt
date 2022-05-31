package io.kamara.backpackdemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.skyscanner.backpack.calendar2.BpkCalendar
import net.skyscanner.backpack.calendar2.CalendarEffect
import net.skyscanner.backpack.calendar2.CalendarParams
import net.skyscanner.backpack.calendar2.CalendarSelection
import net.skyscanner.backpack.toast.BpkToast
import net.skyscanner.backpack.util.unsafeLazy
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.Period
import org.threeten.bp.YearMonth

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    private val calendar by unsafeLazy { requireView().findViewById<BpkCalendar>(R.id.calendar2)!! }
    private val now = LocalDate.of(2019, 1, 1)
    private val range = now..(now + Period.ofYears(2))

    private var scope: CoroutineScope? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope?.cancel()
        scope = CoroutineScope(Dispatchers.Main)


        calendar.state
            .filter { it.selection !is CalendarSelection.None }
            .onEach {
                BpkToast.makeText(requireContext(), it.selection.toString(), BpkToast.LENGTH_SHORT).show()
            }
            .launchIn(scope!!)

        calendar.effects
            .filter { it is CalendarEffect.MonthSelected }
            .onEach {
                BpkToast.makeText(requireContext(), it.toString(), BpkToast.LENGTH_SHORT).show()
            }
            .launchIn(scope!!)

        calendar.setParams(
            CalendarParams(
                now = now,
                range = range,
                selectionMode = CalendarParams.SelectionMode.Range(true),
                wholeMonthSelectionLabel = "Select whole month"
            ),
        )
        calendar.setSelection(CalendarSelection.Range.Month(YearMonth.of(2019, Month.JANUARY)))

    }

    override fun onDestroyView() {
        super.onDestroyView()
        scope?.cancel()
        scope = null
    }
}