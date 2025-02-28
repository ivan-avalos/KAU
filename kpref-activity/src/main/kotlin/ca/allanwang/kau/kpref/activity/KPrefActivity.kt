/*
 * Copyright 2018 Allan Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.allanwang.kau.kpref.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import ca.allanwang.kau.animators.KauAnimator
import ca.allanwang.kau.animators.SlideAnimatorAdd
import ca.allanwang.kau.animators.SlideAnimatorRemove
import ca.allanwang.kau.internal.KauBaseActivity
import ca.allanwang.kau.kotlin.lazyUi
import ca.allanwang.kau.kpref.activity.databinding.KauPrefActivityBinding
import ca.allanwang.kau.kpref.activity.items.KPrefItemCore
import ca.allanwang.kau.ui.views.RippleCanvas
import ca.allanwang.kau.utils.*
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.getSelectExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

abstract class KPrefActivity : KauBaseActivity(), KPrefActivityContract {

    private val adapter: FastItemAdapter<KPrefItemCore> = FastItemAdapter()
    val bgCanvas: RippleCanvas get() = binding.kauRipple
    val toolbarCanvas: RippleCanvas get() = binding.kauToolbarRipple
    val toolbar: Toolbar get() = binding.kauToolbar
    private lateinit var globalOptions: GlobalOptions
    private val kprefStack = Stack<Pair<Int, List<KPrefItemCore>>>()

    /**
     * Toggle sliding animations for the kpref items
     */
    var animate: Boolean = true

    private val recyclerAnimatorNext: KauAnimator by lazyUi {
        KauAnimator(
            SlideAnimatorAdd(KAU_RIGHT, itemDelayFactor = 0f),
            SlideAnimatorRemove(KAU_LEFT, itemDelayFactor = 0f)
        )
    }
    private val recyclerAnimatorPrev: KauAnimator by lazyUi {
        KauAnimator(
            SlideAnimatorAdd(KAU_LEFT, itemDelayFactor = 0f),
            SlideAnimatorRemove(KAU_RIGHT, itemDelayFactor = 0f)
        )
    }

    /**
     * Core attribute builder that is consistent throughout all items
     * Leave blank to use defaults
     */
    abstract fun kPrefCoreAttributes(): CoreAttributeContract.() -> Unit

    private lateinit var binding: KauPrefActivityBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setup layout
        binding = KauPrefActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.init(savedInstanceState)
    }

    @SuppressLint("NewApi")
    private fun KauPrefActivityBinding.init(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            toolbar.setNavigationOnClickListener { onBackPressed() }
            setDisplayShowTitleEnabled(false)
        }
        findViewById<View>(android.R.id.content).setOnApplyWindowInsetsListener { _, insets ->
            kauToolbar.setMarginTop(insets.systemWindowInsetTop)
            insets
        }
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        statusBarColor = 0x30000000
        kauToolbarRipple.set(resolveColor(R.attr.colorPrimary))
        kauRipple.set(resolveColor(android.R.attr.colorBackground))
        // setup prefs
        val core = CoreAttributeBuilder()
        val builder = kPrefCoreAttributes()
        core.builder()
        globalOptions = GlobalOptions(core, this@KPrefActivity)
        kauRecycler.withLinearAdapter(adapter)
        adapter.apply {
            getSelectExtension().isSelectable = true
            onClickListener = { v, _, item, _ ->
                item.onClick(v!!)
                true
            }
        }
        showNextPrefs(R.string.kau_settings, onCreateKPrefs(savedInstanceState), true)
    }

    override fun showNextPrefs(@StringRes toolbarTitleRes: Int, builder: KPrefAdapterBuilder.() -> Unit) =
        binding.showNextPrefs(toolbarTitleRes, builder, false)

    private fun KauPrefActivityBinding.showNextPrefs(
        @StringRes toolbarTitleRes: Int,
        builder: KPrefAdapterBuilder.() -> Unit,
        first: Boolean
    ) {
        launch {
            val items = withContext(Dispatchers.Default) {
                val items = KPrefAdapterBuilder(globalOptions)
                builder(items)
                kprefStack.push(toolbarTitleRes to items.list)
                items.list
            }
            kauRecycler.itemAnimator = if (animate && !first) recyclerAnimatorNext else null
            show(toolbarTitleRes, items)
        }
    }

    private fun show(@StringRes toolbarTitleRes: Int, items: List<KPrefItemCore>) {
        toolbar.setTitle(toolbarTitleRes)
        adapter.clear()
        adapter.add(items.filter { it.core.visible() })
    }

    /**
     * Pops the stack and loads the next kpref list
     * Indices are not checked so ensure that this is possible first
     */
    override fun showPrevPrefs() {
        kprefStack.pop()
        val (title, list) = kprefStack.peek()
        binding.kauRecycler.itemAnimator = if (animate) recyclerAnimatorPrev else null
        show(title, list)
    }

    /**
     * Check if it's possible to go back a stack
     */
    override val hasPrevPrefs
        get() = kprefStack.size > 1

    /**
     * Reload the current pref list from the stack.
     * This will adjust the list of items change in visibility
     */
    fun reloadList() {
        // If for some reason we are calling a reload before fetching our first kpref list, we will ignore it
        if (kprefStack.size < 1) return
        binding.kauRecycler.itemAnimator = null
        val list = kprefStack.peek().second
        adapter.setNewList(list.filter { it.core.visible() })
    }

    /**
     * Selectively reload an item based on its index.
     * Note that this might not behave as expected if certain items are not visible,
     * as those items aren't sent to the adapter.
     *
     * For those cases, consider using [reloadByTitle]
     */
    fun reload(vararg index: Int) {
        if (index.isEmpty()) adapter.notifyAdapterDataSetChanged()
        else index.forEach { adapter.notifyItemChanged(it) }
    }

    /**
     * Iterate through all items and reload if it matches any of the titles
     * If multiple items have the same title, they will all be reloaded
     */
    override fun reloadByTitle(@StringRes vararg title: Int) {
        if (title.isEmpty()) return
        adapter.adapterItems.forEachIndexed { index, item ->
            if (title.any { item.core.titleId == it })
                adapter.notifyItemChanged(index)
        }
    }

    abstract fun onCreateKPrefs(savedInstanceState: Bundle?): KPrefAdapterBuilder.() -> Unit

    override fun onBackPressed() {
        if (!backPress()) super.onBackPressed()
    }

    /**
     * Back press handler with status output
     * Returns [true] if the press has been consumed, [false] otherwise
     */
    fun backPress(): Boolean {
        if (hasPrevPrefs) {
            showPrevPrefs()
            return true
        }
        return false
    }
}
