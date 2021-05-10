package com.mikhail.vyakhirev.presentation.login_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.model.AuthType
import com.mikhail.vyakhirev.data.model.MaterialDialogContent
import com.mikhail.vyakhirev.data.model.toAuthType
import com.mikhail.vyakhirev.databinding.LoginFragmentBinding
import com.mikhail.vyakhirev.utils.CustomMaterialDialog.showSimpleDialog
import com.mikhail.vyakhirev.utils.CustomMaterialDialog.showSingleChoiceDialog
import com.mikhail.vyakhirev.utils.RC_GOOGLE_SIGN_IN_CODE
import com.mikhail.vyakhirev.utils.extensions.applyColorToAllDescendantsAndDisableState
import com.mikhail.vyakhirev.utils.extensions.enableAllDescendantsAndApplyPreviousColor
import com.mikhail.vyakhirev.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var binding: LoginFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginFragmentBinding = LoginFragmentBinding.inflate(inflater, container, false)
        binding = loginFragmentBinding

        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = getString(R.string.login_fragment_label)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.cancelBTN?.setOnClickListener {
            findNavController().navigate(R.id.listMyFragment)
        }

        binding?.loginBtn?.setOnClickListener {
            viewModel.myLogin(binding?.userEditText?.text.toString(),binding?.passwordEditText?.text.toString() )
        }

        viewModel.isLogged.observe(viewLifecycleOwner,{
            if (it.peekContent())
                findNavController().navigate(R.id.listMyFragment)
            else
               toast(getString(R.string.inccorect_login))
        })

        binding?.registrationBtn?.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }

        binding?.socialFacebookButton?.setOnClickListener {
            doSocialAuth(AuthType.FACEBOOK)
        }

        binding?.socialGithubButton?.setOnClickListener {
            doSocialAuth(AuthType.GITHUB)
        }

        binding?.socialGoogleButton?.setOnClickListener {
            doSocialAuth(AuthType.GOOGLE)
        }

        binding?.socialTwitterButton?.setOnClickListener {
            doSocialAuth(AuthType.TWITTER)
        }

        viewModel.uiState.observe(viewLifecycleOwner, { authModel ->
//            loginProgressBar.visible(authModel.showProgress)

            if (authModel.success) {
                findNavController().navigate(R.id.listMyFragment)
            } else if (authModel.error != null && !authModel.error.consumed)
                authModel.error.consume()?.let { pair ->
                    showSimpleDialog(requireActivity(), pair.second) {
                        doSocialAuth(pair.first)
                    }
//                    socialIncludeLayout.enableAllDescendantsAndApplyPreviousColor<MaterialButton>()
                }
            else if (authModel.showAllLinkProvider != null && !authModel.showAllLinkProvider.consumed)
                authModel.showAllLinkProvider.consume()?.let { pair ->
                    showSingleChoiceDialog(
                        requireActivity(), pair.second, pair.first,
                        negativeButtonClickListener = {
//                            socialIncludeLayout.enableAllDescendantsAndApplyPreviousColor<MaterialButton>()
                        }) {
                        val authType = it.toAuthType()
                        doSocialAuth(authType)
                    }
                }
        })

    }

    private fun doSocialAuth(authType: AuthType) {
        when (authType) {
            AuthType.GOOGLE -> viewModel.googleSignIn(requireContext()).also {
                startActivityForResult(it, RC_GOOGLE_SIGN_IN_CODE)
            }
            AuthType.TWITTER -> {
//                disableButtons(socialIncludeLayout)
                viewModel.doTwitterAuthentication(requireActivity())
            }
            AuthType.FACEBOOK -> {
//                disableButtons(socialIncludeLayout)
                viewModel.loginManager.logInWithReadPermissions(this, facebook_permissions)
            }
            AuthType.EMAIL -> {
//                disableButtons(socialIncludeLayout)
                viewModel.fetchAllProviderForEmail(authType.authValue)
            }
            AuthType.GITHUB -> {
//                disableButtons(socialIncludeLayout)
                viewModel.doGithubAuthentication(requireActivity())
            }
        }
    }

    private fun disableButtons(views: ViewGroup) {
        views.applyColorToAllDescendantsAndDisableState<MaterialButton>(R.color.color_on_surface_emphasis_disabled)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val direction = LoginFragmentDirections.actionLoginFragmentToListMyFragment()
            view?.findNavController()?.navigate(direction)
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.handleOnActivityResult(requestCode, resultCode, data)
        viewModel.loadUserData()
    }

    private companion object {
        private val facebook_permissions = mutableListOf("email", "public_profile")
    }

}