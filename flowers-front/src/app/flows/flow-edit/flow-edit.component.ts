import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {FlowService} from '../services/flow.service';
import {MATERIAL_IMPORTS} from '../../shared/material/material.imports';
import {FlowSubscriber} from '../model/flow-subscriber';
import {Destination} from '../model/destination';
import {NgIf} from '@angular/common';
import {Subscription} from 'rxjs';


@Component({
  selector: 'app-flow-edit',
  imports: [
    MATERIAL_IMPORTS,
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './flow-edit.component.html',
  styleUrl: './flow-edit.component.scss'
})
export class FlowEditComponent implements OnInit , OnDestroy {
  private subscription: Subscription = new Subscription();
  flowForm!: FormGroup; // Reactive form
  id!: number; // Capturing the ID from route
  subscribers: FlowSubscriber[] = [];
  publishers: Destination[] = [];

  constructor(
    private flowService: FlowService,
    private fb: FormBuilder,
    private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    // Get the ID from the route
    this.id = +this.route.snapshot.paramMap.get('id')!;

    // Initialize the form with default values
    this.flowForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      publishers: this.fb.array([]), // Placeholder for publishers
      subscribers: this.fb.array([]), // Placeholder for subscribers
      enabled: [true, Validators.required],
      status: ['started', Validators.required] // Default status
    });

    // Simulate loading the flow data
    this.loadFlow(this.id);
  }

  /**
   * Load flow data (fake API or service call)
   */
  loadFlow(id: number): void {
    // Example of pre-loaded data (replace this with a service call to fetch real data)
    this.flowService.getById(id).subscribe(flow => {
        this.flowForm.patchValue({
          name: flow.name,
          enabled: flow.enabled,
          status: flow.status,
          publishers: flow.publishers,
          subscribers: flow.subscribers
        });

        this.publishers = flow.publishers;
        this.subscribers = flow.subscribers;
      }
    )

  }

  /**
   * Handle form submission
   */
  onSubmit(): void {
    if (this.flowForm.valid) {
      console.log('Updated Flow:', this.flowForm.value);
      // Save your updated flow object here (send it to a service or API)
    } else {
      console.log('Form is invalid!');
    }
  }

  onCancel() {

  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

}

