package echo.com.surveys.fragment

import android.support.v4.app.Fragment

abstract class BaseFragment : Fragment() {
    protected abstract fun handleBundle()
}
